package com.salakheev.shaderbuilderkt

import com.salakheev.shaderbuilderkt.annotations.ShaderProgram
import com.x5.template.Theme
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.lang.model.SourceVersion

/**
 * Searches for Classes annotated with [ShaderProgram] and generates sources named <ClassName>Sources
 * with the same package and module a source is from.
 *
 * During the generation process it will try to find constructor parameters of classes [vertex], [fragment]
 * and associate them with constructor parameters of an annotated Class.
 *
 * Classes [ShaderProgram.vertex] and [ShaderProgram.fragment] must be accessible
 * for [ShaderBuilderProcessor] to instantiate them, thus have to be either
 * stored along with it or in a dependent module.
 */
@SupportedAnnotationTypes("com.salakheev.shaderbuilderkt.annotations.ShaderProgram")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ShaderBuilderProcessor : AbstractProcessor() {
	override fun process(annotations: MutableSet<out TypeElement>, env: RoundEnvironment): Boolean {
		val elements = env.getElementsAnnotatedWith(ShaderProgram::class.java)
		val shaderPrograms = ElementFilter.typesIn(elements)
		shaderPrograms.forEach { generateProgram(it) }
		return shaderPrograms.isEmpty()
	}

	private fun generateProgram(type: TypeElement) {
		val theme = Theme("/resources/themes", "program")
		val chunk = theme.makeChunk("program")
		val processor = ProgramProcessor(processingEnv, type, chunk)
		writeSourceFile(processor.getPackageName(), processor.getClassName(), processor.getResult())
	}

	private fun writeSourceFile(classPackage: String, className: String, text: String) {
		val filePath = processingEnv.options.getValue(KAPT_KOTLIN_GENERATED)
		File("$filePath/$classPackage", "$className.kt").apply {
			parentFile.mkdirs()
			writeText(text)
		}
	}

	private companion object {
		const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
	}
}
