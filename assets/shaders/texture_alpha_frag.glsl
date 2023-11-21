#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

uniform float textureAlphaScaling1;
uniform float textureAlphaScaling2;

uniform float textureDimensionScaling1;
uniform float textureDimensionScaling2;

uniform float xOffset1;
uniform float xOffset2;

varying vec4 v_color;
varying vec2 v_texCoord;

void main()
{
    vec2 newCoord1;
    newCoord1.x = (xOffset1 + v_texCoord.x) / textureDimensionScaling1;
    newCoord1.y = v_texCoord.y / textureDimensionScaling1;

    vec2 newCoord2;
    newCoord2.x = (xOffset2 + v_texCoord.x) / textureDimensionScaling2;
    newCoord2.y = v_texCoord.y / textureDimensionScaling2;

    vec4 textureColor1 = texture2D(u_texture, newCoord1);
    vec4 textureColor2 = texture2D(u_texture, newCoord2);

    textureColor1.a = textureAlphaScaling1 * textureColor1.a;
    textureColor2.a = textureAlphaScaling2 * textureColor2.a;

    gl_FragColor = mix(textureColor1, textureColor2, textureColor2.a);

}
