#type vertex
#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aTextCoords;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTextCoords;

void main()
{
    fColor = aColor;
    fTextCoords = aTextCoords;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0f);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTextCoords;

out vec4 color;

uniform float uTime;
uniform sampler2D TEXTURE_SAMPLER;

void main()
{
    color = texture(TEXTURE_SAMPLER, fTextCoords);
}