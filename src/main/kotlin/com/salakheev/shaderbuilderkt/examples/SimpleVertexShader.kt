package com.salakheev.shaderbuilderkt.examples

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.examples.components.ShadowReceiveComponent
import com.salakheev.shaderbuilderkt.builder.types.mat.Mat4
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec2
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec3

class SimpleVertexShader(receiveShadow: Boolean) : ShaderBuilder() {
	private val mvp by uniform(::Mat4)

	private val vertex by attribute(::Vec3)
	private val uv by attribute(::Vec2)

	private var vUV by varying(::Vec2)

	init {
		val inp by vec4(vertex, 1.0f)
		vUV = uv
		if (receiveShadow) {
			val shadowReceiveComponent = ShadowReceiveComponent()
			shadowReceiveComponent.vertex(this, inp)
		}
		gl_Position = mvp * inp
	}
}
