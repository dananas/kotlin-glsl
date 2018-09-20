import com.salakheev.shaderbuilderkt.annotations.ShaderProgram
import com.salakheev.shaderbuilderkt.examples.SimpleFragmentShader
import com.salakheev.shaderbuilderkt.examples.SimpleVertexShader

/**
 * Here we use [ShaderProgram] annotation to generate [SimpleShaderProgramSources]
 * using gradle kaptKotlin task.
 *
 * @see [ShaderProgram]
 */
@ShaderProgram(SimpleVertexShader::class, SimpleFragmentShader::class)
class SimpleShaderProgram(receiveShadow: Boolean, alphaTest: Boolean)

/**
 * Before running use gradle build.
 */
fun main(args: Array<String>) {
    /**
     * [SimpleShaderProgramSources] was generated with all possible shader variants during gradle build.
     */
    println(SimpleShaderProgramSources.get(true, false).vertex)

    /**
     * Or you can get GLSL source directly from [ShaderBuilder] instance
     */
    println(SimpleVertexShader(true).getSource())
}