package com.salakheev.shaderbuilderkt.examples

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.sampler.Sampler2D
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLFloat
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec2

class FragmentShader(useAlphaTest: Boolean) : ShaderBuilder() {
    private val alphaTestThreshold by uniform(::GLFloat)
    private val texture by uniform(::Sampler2D)
    private val uv by varying(::Vec2)

    init {
        var color by vec4()
        color = texture2D(texture, uv)
        if (useAlphaTest) {
            If(color.w lt alphaTestThreshold) {
                discard()
            }
        }
        gl_FragColor = color
    }
}