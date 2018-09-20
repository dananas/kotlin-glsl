package com.salakheev.shaderbuilderkt

import com.salakheev.shaderbuilderkt.annotations.ShaderIntRange
import com.salakheev.shaderbuilderkt.annotations.ShaderProgram
import com.salakheev.shaderbuilderkt.sources.ShaderSourceProvider
import com.x5.template.Chunk
import java.lang.IllegalArgumentException
import javax.activation.UnsupportedDataTypeException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

/**
 * Processes constructor of a [ShaderProgram] annotated class.
 * Collects all possible values for the constructor and generates shader program sources
 * using 'resources/themes/program/program.chtml' template.
 */
class ProgramProcessor(private val env: ProcessingEnvironment, private val programType: TypeElement, private val chunk: Chunk) {
	private val sourceClassName = programType.simpleName
	private val generatedClassName = "${sourceClassName}Sources"
	private val vertex: TypeElement
	private val fragment: TypeElement

	init {
		val packageName = programType.getPackageName()
		if (packageName.isNotEmpty()) {
			chunk.set("package", packageName)
		}
		chunk.set("sourceClass", programType.qualifiedName)
		chunk.set("className", generatedClassName)
		chunk.set("sourceName", sourceClassName)

		val annotation = programType.getAnnotation(ShaderProgram::class.java)
		vertex = getAnnotationValue(annotation) { a -> a.vertex }
		fragment = getAnnotationValue(annotation) { a -> a.fragment }

		val programConstructor = getTypeConstructor(programType)
		processConstructor(programConstructor)
	}

	private fun getTypeConstructor(type: TypeElement): ExecutableElement {
		return type.enclosedElements.find { elem -> elem.kind == ElementKind.CONSTRUCTOR } as ExecutableElement
	}

	private fun processConstructor(constructor: ExecutableElement) {
		val parameters = constructor.parameters.map { parameter -> parseParameter(parameter) }
		val list = parameters.map { "${it.name}: ${it.type}" }
		if (list.isNotEmpty()) {
			chunk.set("paramsWithTypes", list)
		}
		chunk.set("resultBranches", getBranches(parameters))
	}

	private fun convertToKotlinType(typeName: String): String {
		return when (typeName) {
			"boolean" -> "Boolean"
			"int" -> "Int"
			else -> typeName
		}
	}

	private fun parseParameter(param: VariableElement): Parameter {
		val typeName = param.asType().toString()
		return Parameter(convertToKotlinType(typeName), param.simpleName.toString(), when (typeName) {
			"boolean" -> BooleanParams().values()
			"int" -> getPossibleIntValues(param)
			else -> throw UnsupportedDataTypeException("Type '$typeName' is not supported as @ShaderProgram parameter type")
		})
	}

	private fun getPossibleIntValues(param: VariableElement): Array<*> {
		val annotation = param.getAnnotation(ShaderIntRange::class.java)
		return when (annotation) {
			null -> throw UnsupportedDataTypeException("@ShaderProgram type parameter with '${param.asType()}' type must have @ShaderIntRange annotation")
			else -> {
				with (annotation) {
					if (from < 1) throw IllegalArgumentException("@ShaderIntRange: 'from' < 1")
					if (from > to) throw IllegalArgumentException("@ShaderIntRange: 'from' > 'to'")
					if (to - from > 63) throw IllegalArgumentException("@ShaderIntRange: provided range is too big! Maximum supported is 64 elements")
					IntParameterParams(from, to).values()
				}
			}
		}
	}

	private fun getBranches(parameters: List<Parameter>): List<Branch> {
		val result = mutableListOf<Branch>()
		collectBranches(parameters, emptyMap(), result)
		return result
	}

	private fun collectBranches(parameters: List<Parameter>, paramValues: Map<String, Any>, result: MutableCollection<Branch>) {
		if (parameters.isEmpty()) {
			val sources = getShaderSources(paramValues)
			result.add(Branch(paramValues.entries.joinToString(" and ") { "(${it.key} == ${it.value})" }, result.size, sources.first, sources.second))
		} else {
			val parameter = parameters.first()
			parameter.possibleValues.forEach { value ->
				collectBranches(parameters.drop(1), paramValues + (parameter.name to value!!), result)
			}
		}
	}

	private fun getShaderSources(paramValues: Map<String, Any>): Pair<String, String> {
		val classLoader = javaClass.classLoader
		val vertexClass = classLoader.loadClass(vertex.qualifiedName.toString())
		val fragmentClass = classLoader.loadClass(fragment.qualifiedName.toString())
		val vertexSource = prepareShaderSource(vertex, vertexClass, paramValues)
		val fragmentSource = prepareShaderSource(fragment, fragmentClass, paramValues)
		return Pair(vertexSource, fragmentSource)
	}

	private fun prepareShaderSource(typeElement: TypeElement, shaderClass: Class<*>, paramValues: Map<String, Any>): String {
		val element = getTypeConstructor(typeElement)
		val programConstructor = getTypeConstructor(programType)
		val shaderClassName = typeElement.qualifiedName.toString()

		checkProgramHasShaderParams(programConstructor, element, shaderClassName)

		if (shaderClass.constructors.isEmpty()) {
			throw IllegalAccessException("Class '$shaderClassName' doesn't have constructor")
		}
		val constructor = shaderClass.constructors[0]
		val params = element.parameters.map { paramValues.getValue(it.simpleName.toString()) }.toTypedArray()
		val instance = constructor.newInstance(*params)
		val provider = instance as ShaderSourceProvider
		return provider.getSource()
	}

	private fun checkProgramHasShaderParams(programConstructor: ExecutableElement, shaderConstructor: ExecutableElement, shaderClassName: String) {
		shaderConstructor.parameters.forEach { shaderParam ->
			val shaderParamName = shaderParam.simpleName.toString()
			val programParam = programConstructor.parameters.find { it.simpleName.toString() == shaderParamName }
			if (programParam == null) {
				throw IllegalArgumentException("Shader program '$sourceClassName' doesn't have parameter '$shaderParamName' provided in '$shaderClassName'")
			}
			if (programParam.asType().toString() != shaderParam.asType().toString()) {
				throw IllegalArgumentException("Shader program '$sourceClassName' and '$shaderClassName' have different types of '$shaderParamName'")
			}
		}
	}

	private fun asTypeElement(typeMirror: TypeMirror) = env.typeUtils.asElement(typeMirror) as TypeElement

	private fun <T>getAnnotationValue(annotation: T, processor: (T) -> Unit): TypeElement {
		try {
			processor(annotation)
		} catch (e: MirroredTypeException) {
			return asTypeElement(e.typeMirror)
		}
		throw Error()
	}

	fun getPackageName() = programType.getPackageName()
	fun getClassName() = generatedClassName
	fun getResult() = chunk.toString()

	private fun TypeElement.getPackageName(): String {
		if (qualifiedName.contains(".")) {
			return qualifiedName.substring(0, qualifiedName.lastIndexOf('.'))
		}
		return ""
	}

	private interface ParameterValues {
		fun values(): Array<*>
	}

	private class IntParameterParams(val from: Int, val to: Int): ParameterValues {
		override fun values(): Array<Int> {
			return Array(to - from + 1) { i ->
				i + from
			}
		}
	}
	private class BooleanParams: ParameterValues {
		override fun values(): Array<Boolean> {
			return arrayOf(false, true)
		}
	}

	internal class Branch(val params: String, val index: Int, val vertex: String, val fragment: String)
	private class Parameter(val type: String, val name: String, val possibleValues: Array<*>)
}
