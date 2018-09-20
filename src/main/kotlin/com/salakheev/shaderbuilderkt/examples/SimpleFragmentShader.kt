package com.salakheev.shaderbuilderkt.examples

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.examples.components.ShadowReceiveComponent
import com.salakheev.shaderbuilderkt.builder.types.sampler.Sampler2D
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLFloat
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec2

class SimpleFragmentShader(alphaTest: Boolean, receiveShadow: Boolean) : ShaderBuilder() {
    private val alphaTestThreshold by uniform(::GLFloat)
    private val texture by uniform(::Sampler2D)

    private val vUV by varying(::Vec2)

    init {
        var color by vec4()
        color = texture2D(texture, vUV)
        if (alphaTest) {
            If(color.w lt alphaTestThreshold) {
                discard()
            }
        }
        var brightness by float(1.0f)
        if (receiveShadow) {
            val shadowReceiveComponent = ShadowReceiveComponent()
            brightness = shadowReceiveComponent.fragment(this, brightness)
        }
        brightness = clamp(brightness, 0.5f, 1.0f)
        color.xyz = color.xyz * brightness
        gl_FragColor = color
    }
}