        #type vertex
        #version 330 core
        layout (location=0) in vec3 aPos;
        layout (location= 1) in vec3 aColor;
        layout (location= 2) in vec2 aTexCoord;

        out vec3 ourColor;
        out vec2 TexCoord;

        void main() {
                gl_Position = vec4(aPos.x, aPos.y , aPos.z, 1.0F);
                ourColor = aColor;
                TexCoord = aTexCoord;
        }

        #type fragment
        #version 330 core
        out vec4 frag_colour;

        in vec3 ourColor;
        in vec2 TexCoord;

        uniform sampler2D ourTexture;
        uniform sampler2D ourTxt2;
        uniform float control;

        void main() {
//            frag_colour = vec4(ourColor, 1.0F);
                frag_colour = mix(texture(ourTexture, TexCoord) , texture(ourTxt2, vec2(1-TexCoord.x, TexCoord.y)), control);
        }