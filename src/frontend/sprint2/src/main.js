import './styles/common.scss'
import '@mdi/font/css/materialdesignicons.css';
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'



import App from './App.vue'
import router from './router'
import { useIntersectionObserver } from '@vueuse/core'

const app = createApp(App)
const vuetify = createVuetify({
    components,
    directives,
  })


app.use(createPinia())
app.use(router)
app.use(vuetify);
app.mount('#app')
