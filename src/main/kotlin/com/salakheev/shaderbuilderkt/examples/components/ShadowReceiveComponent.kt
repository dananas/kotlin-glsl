package com.salakheev.shaderbuilderkt.examples.components

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.ShaderBuilderComponent
import com.salakheev.shaderbuilderkt.builder.types.mat.Mat4
import com.salakheev.shaderbuilderkt.builder.types.sampler.Sampler2D
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLFloat
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec4

/**
 * An example of [ShaderBuilderComponent]
 */
class ShadowReceiveComponent : ShaderBuilderComponent() {
	private val shadowMVP by uniform(::Mat4)
	private val shadowTexture by uniform(::Sampler2D)

	private var vShadowCoord by varying(::Vec4)

	fun vertex(parent: ShaderBuilder, inp: Vec4) {
		vShadowCoord = shadowMVP * inp
		vShadowCoord.y = -vShadowCoord.y
		val offset by vec2(1f, 1f)
		vShadowCoord.xy = (vShadowCoord.xy + offset) / 2f
		parent.appendComponent(this)
	}

	fun fragment(parent: ShaderBuilder, brightness: GLFloat): GLFloat {
		val bias by float(0.0035f)
		val shadowStep by float(0.4f)

		var newBrightness by float(brightness)

		var pixel by float()
		pixel = texture2D(shadowTexture, vShadowCoord.xy).x
		If(pixel lt (vShadowCoord.z + 1.0f) / 2.0f - bias) {
			newBrightness = newBrightness - shadowStep
		}
		parent.appendComponent(this)
		return newBrightness
	}
}
