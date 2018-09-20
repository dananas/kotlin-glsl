# GLSL shaders development in Kotlin

The sources provide a way of creating GLSL-like code in Kotlin.

Shader source in <b>Kotlin</b>:
```kotlin
class FragmentShader(useAlphaTest: Boolean) : ShaderBuilder() {
    private val alphaTestThreshold by uniform(::GLFloat)
    private val texture by uniform(::Sampler2D)
    private val uv by varying(::Vec2)
    init {
        var color by vec4()
        color = texture2D(texture, uv)
        // static branching
        if (useAlphaTest) {
            // dynamic branching
            If(color.w lt alphaTestThreshold) {
                discard()
            }
        }
        gl_FragColor = color
    }
}
```
You can get your GLSL source in two ways:
#### 1. Runtime generation
```kotlin
FragmentShader(useAlphaTest = true).getSource()
```
Pros: easy to generate sources.

Cons: GLSL <a href="https://www.khronos.org/opengl/wiki/Data_Type_(GLSL)#Swizzling">swizzling</a> functionality may affect performance, as it creates lots of objects at runtime.

#### 2. Generation using ```@ShaderProgram``` annotation
This functionality can be used as follows:
```kotlin
@ShaderProgram(VertexShader::class, FragmentShader::class)
class ShaderProgramName(alphaTest: Boolean)
```

Annotation processor generates new sources during ```gradle kaptKotlin``` task which  look as follows:
```kotlin
class ShaderProgramNameSources {
    enum class Sources(vertex: String, fragment: String): ShaderProgramSources {
        Source0("<vertex code>", "<fragment code>")
        ...
    }
    fun get(alphaTest: Boolean) {
        if (alphaTest) return Source0
        else return Source1
    }
}
```
After generation you can get both vertex and fragment sources as follows:
```kotlin
val sources = ShaderProgramNameSources.get(replaceAlpha = true)
println(sources.vertex)
println(sources.fragment)
```
Pros: no need to generate sources at runtime, as you will have direct access to them.

Cons: shader source classes have to be accessible for <u><abbr title="Annotation Processor">AP</abbr></u> to load them, thus can't be stored just anywhere in the project.

#### Generated GLSL
Both methods produce the same result:
```glsl
uniform sampler2D texture;
uniform float alphaTestThreshold;
varying vec2 uv;
void main(void) {
    vec4 color;
    color = texture2D(texture, uv);
    if ((color.w < alphaTestThreshold)) {
        discard;
    }
    gl_FragColor = color;
}
```

## How to use
You will need <a href="https://github.com/gradle/gradle">gradle</a> to build sources.

You have two options here. If you <b>do not</b> need ```@ShaderProgram``` annotation functionality in the above example, then you can simply import the <b>KotlinGlsl</b> project (and may remove ProgramExample module).

If you <b>do</b> want to use ```@ShaderProgram```, then you will have to store your Kotlin shader sources either directly in the <b>KotlinGlsl</b> or in a project accessible for it during annotations processing stage, as <u><abbr title="Annotation Processor">AP</abbr></u> will try to load classes provided with ```@ShaderProgram```.

## License
MIT
