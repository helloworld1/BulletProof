#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float textureAlpha;

varying vec4 v_color;
varying vec2 v_texCoord;

void main()
{
    vec4 textureColor = texture2D(u_texture, v_texCoord);

    // The texture's green channel is also alpha
    // for etc1 texture compressiion
    //  We also apply textureAlpha here.
    // textureColor.a = textureColor.g * textureAlpha;

    gl_FragColor = textureAlpha * textureColor;
}
