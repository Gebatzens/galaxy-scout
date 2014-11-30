#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform int border;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    vec4 baseColor = vec4(v_color.rgb, texture2D(u_texture, v_texCoord).a);
    
    float distAlphaMask = baseColor.a;
    if(border == 1 && distAlphaMask >= 0.5 && distAlphaMask <= 0.58) {
      float oFactor = smoothstep(0.52, 0.58, distAlphaMask);
      baseColor = mix(vec4(0, 0, 0, 1), baseColor, oFactor);
    } 
    
    baseColor.a = distAlphaMask >= 0.5 ? 1.0 : 0.0;
    baseColor.a *= v_color.a;
    
    gl_FragColor = baseColor;
} 
