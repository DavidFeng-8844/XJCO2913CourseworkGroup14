<template>
  <div class="map-container">
    <div id="map"></div>

    <!-- 模态框 -->
    <el-dialog v-model="centerDialogVisible" title="滑板车详情" width="400px" center>
      <template #default>
        <div class="dialog-box">
          <div class="info-left">
            <div class="scooter-id">{{ selectedScooter.name }}</div>
            <div class="battery">
              <img :src="getBatteryIcon(selectedScooter.battery)" class="battery-icon" />
              <span > {{ (selectedScooter.battery * 100).toFixed(0) }}%</span>
            </div>
          </div>
          <img :src="getScooterImage(selectedScooter.type)" class="scooter-preview" />
        </div>
        <div class="distance">距你约 {{ selectedScooter.distance?.toFixed(2) || '—' }} 公里</div>
      </template>

      <template #footer>
        <el-button @click="centerDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import mapboxgl from 'mapbox-gl'
import 'mapbox-gl/dist/mapbox-gl.css'

const centerDialogVisible = ref(false)
const selectedScooter = ref({})

const MAPBOX_TOKEN = 'pk.eyJ1IjoidnJ5a2VhbXkiLCJhIjoiY205OW9sZDNuMGdqbDJqb28waHI3bjdldSJ9.QY8BWeP0vTnGZV6w4YhunA'
const MAPBOX_STYLE = 'mapbox://styles/vrykeamy/cm9bnowrp004j01s76rsx0i7x'
const CENTER = [-1.5491, 53.8008]
const nameMap = {
  scooter1: 'SwiftOne',
  scooter2: 'SwiftX',
  scooter3: 'VoltRunnerX'
}

function getScooterImage(type) {
  switch (type) {
    case 'scooter2': return new URL('@/assets/images/scooter/scooter2.png', import.meta.url).href
    case 'scooter3': return new URL('@/assets/images/scooter/scooter3.png', import.meta.url).href
    default: return new URL('@/assets/images/scooter/scooter1.png', import.meta.url).href
  }
}

function getBatteryIcon(level) {
  const percent = Math.floor(level * 100)
  if (percent <= 25) return new URL('@/assets/images/battery-1.png', import.meta.url).href
  else if (percent <= 50) return new URL('@/assets/images/battery-2.png', import.meta.url).href
  else if (percent <= 75) return new URL('@/assets/images/battery-3.png', import.meta.url).href
  else return new URL('@/assets/images/battery-4.png', import.meta.url).href
}

function generateScooters(centerLng, centerLat) {
  const scooters = []
  for (let i = 0; i < 15; i++) {
    const offsetLng = (Math.random() - 0.5) * 0.006
    const offsetLat = (Math.random() - 0.5) * 0.006
    const type = i < 5 ? 'scooter1' : i < 10 ? 'scooter2' : 'scooter3'
    scooters.push({
      lng: centerLng + offsetLng,
      lat: centerLat + offsetLat,
      battery: [1.0, 0.5, 0.25, 0.1, 0.75][i % 5],
      name: `${nameMap[type]}···${i % 5 + 1}`,
      type
    })
  }
  return scooters
}

function getDistance(lat1, lon1, lat2, lon2) {
  const R = 6371 // km
  const dLat = (lat2 - lat1) * Math.PI / 180
  const dLon = (lon2 - lon1) * Math.PI / 180
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2)
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return R * c
}

onMounted(() => {
  mapboxgl.accessToken = MAPBOX_TOKEN
  const map = new mapboxgl.Map({
    container: 'map',
    style: MAPBOX_STYLE,
    center: CENTER,
    zoom: 16
  })

  map.addControl(new mapboxgl.NavigationControl(), 'bottom-right')

  const userEl = document.createElement('div')
  userEl.className = 'user-marker'
  new mapboxgl.Marker(userEl)
    .setLngLat(CENTER)
    .setPopup(new mapboxgl.Popup().setText('你在这里'))
    .addTo(map)

  const scooters = generateScooters(CENTER[0], CENTER[1])
  scooters.forEach((scooter) => {
    const el = document.createElement('div')
    el.className = 'scooter-marker'
    el.style.setProperty('--level', scooter.battery)
    el.innerHTML = `
      <div class="battery-center">
        <div class="scooter-icon ${scooter.type}"></div>
      </div>
    `

    const marker = new mapboxgl.Marker(el)
      .setLngLat([scooter.lng, scooter.lat])
      .addTo(map)

    el.addEventListener('click', () => {
      scooter.distance = getDistance(CENTER[1], CENTER[0], scooter.lat, scooter.lng)
      selectedScooter.value = scooter
      centerDialogVisible.value = true
    })
  })
})
</script>

<style>
.map-container {
  position: absolute;
  inset: 0;
}

#map {
  width: 100%;
  height: 100%;
}

.user-marker {
  width: 24px;
  height: 24px;
  background-color: #2196f3;
  border: 3px solid white;
  border-radius: 50%;
  box-sizing: border-box;
}

.scooter-marker {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: conic-gradient(#00cc33 calc(var(--level) * 1turn), lightgray 0deg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.battery-center {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.scooter-icon {
  width: 18px;
  height: 18px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}
.scooter-icon.scooter1 {
  background-image: url('../../assets/images/scooter/scooter1-icon.png');
}
.scooter-icon.scooter2 {
  background-image: url('../../assets/images/scooter/scooter2-icon.png');
}
.scooter-icon.scooter3 {
  background-image: url('../../assets/images/scooter/scooter3-icon.png');
}

.dialog-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.scooter-id {
  font-size: 18px;
  font-weight: bold;
}

.info-left{
  padding-left: 20px;
}

.battery {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 15px;
  margin-top: 6px;
}

.battery-icon {
  width: 20px;
  height: 20px;
  color: #00cc33;
}

.scooter-preview {
  width: 160px;
  height: auto;
}

.distance {
  text-align: center;
  font-size: 14px;
  color: #666;
}
</style>