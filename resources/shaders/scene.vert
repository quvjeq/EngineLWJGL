#version 330

layout (location=0) in vec3 inPosition;
layout (location=1) in vec3 inColor;

out vec3 ourColor;

uniform mat4 transform;

void main()
{
    gl_Position = transform *   vec4(inPosition, 1.0);
    ourColor = inColor;
}