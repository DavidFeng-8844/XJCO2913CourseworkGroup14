import axios from 'axios'
import { ElMessage } from 'element-plus';
import 'element-plus/theme-chalk/el-message.css'



const http = axios.create({
    baseURL: '',
    timeout: 5000
  });

// axios请求拦截器
// 一般会进行token身份验证等
http.interceptors.request.use(config => {
    return config
  }, e => Promise.reject(e))

// 一般进行错误的统一提示，token失效的处理等
http.interceptors.response.use(res => res.data, e => {
  ElMessage({type:'error', message:e.response.data.message})  
  return Promise.reject(e)
})

export default http