precision mediump float;//精度 为float
varying vec2 v_texPo;//纹理位置  接收于vertex_shader
uniform sampler2D sTexture;//纹理
void main() {
    //rgb
    gl_FragColor = vec4(texture2D(sTexture, v_texPo).rgb, 1);

    //rgba
//    gl_FragColor = texture2D(sTexture, v_texPo);
}
