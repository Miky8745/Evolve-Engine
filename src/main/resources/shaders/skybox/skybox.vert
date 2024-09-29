#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 texCoord;

out vec2 outTextCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 scaledPos = vec4(position * 10, 1.0);

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * scaledPos;
    outTextCoord = texCoord;
}