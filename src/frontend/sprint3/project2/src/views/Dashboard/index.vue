<template>
    <div class="dashboard-container">
      <LayoutHeader :is-transparent="false" />
  
      <h1 class="title">User Dashboard</h1>
  
      <div class="section hire-options-section">
        <h2>Options and Costs</h2>
        <table>
          <thead>
            <tr>
              <th>Time</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(price, duration) in prices" :key="duration">
              <td>{{ duration }}</td>
              <td>${{ price }}</td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <div class="section app">
        <h1>Scooter Maps</h1>
        <div id="map" ref="map" style="height: 500px;"></div>
        <button class="getUserLocation" @click="getUserLocation">Find Nearest Scooter</button>
        <!-- get all scooters -->
        <!-- <button class="getUserLocation" @click="">Find all fetchNearestScooters</button> -->
        <!-- center to user location -->
        <button class="centerToUser" @click="centerToUserLocation">Center map to my Location</button>
        <h2>Avaliable Scooters</h2>
        <ul>
          <li v-for="scooter in nearbyScooters" :key="scooter.id" class="scooter-item">
            <div>
              <strong>ID: {{ scooter.id }}</strong> -
              Status: {{ scooter.status }} -
              Distance: {{ scooter.distance }} km
            </div>
          </li>
        </ul>
      </div>
  
      <div class="section booking-section">
        <h2>Book</h2>
        <div class="input-container">
          <div class="input-field">
            <label for="scooterId">Scooter ID:</label>
            <input type="text" id="scooterId" v-model="scooterId" />
          </div>
  
          <div class="input-field">
            <label for="duration">Duration:</label>
            <select id="duration" v-model="selectedDuration">
              <option v-for="option in options" :key="option.value" :value="option.value">{{ option.text }}</option>
            </select>
          </div>

          <div class="input-field">
            <label for="cardNumber">Card Number:</label>
            <input type="text" id="cardNumber" v-model="cardNumber" placeholder="Enter your card number" />
          </div>
        </div>
  
        <button class="book-button" @click="bookScooter">Book Scooter</button>

        <!-- use radio box to select if new user -->
        <div class="input-field">
          <input type="checkbox" id="newUser" value="new" v-model="isNewUser" />New User
        </div>
        <div v-if="isNewUser">
          <div class="input-field">
            <label for="name">Name:</label>
            <input type="text" id="name" v-model="newUser.name" />
          </div>
          <div class="input-field">
            <label for="email">Email:</label>
            <input type="email" id="email" v-model="newUser.email" />
          </div>
        </div>
        <div class="section my-bookings-section">
          <button class="book-button" @click="getMyBookings">Get My Bookings</button>
          <h2>My booking</h2>
          <ul>
            <li v-for="booking in myBookings" :key="booking.id">
            ID: {{ booking.scooter_id }} - Status: {{ booking.status }} - Start Time: {{ booking.startTime }} - End Time: {{ booking.endTime }} - Duration: {{ booking.durationInHours }} hours - Price: ${{ booking.price }}
              <button class="cancel-button" @click="cancelBooking(booking.id)">Cancel</button>
              <!-- extend booking -->
              <div class="input-field">
                <label for="extendDuration">Extend Duration:</label>
                <select id="extendDuration" v-model="selectedDuration">
                  <option v-for="option in options" :key="option.value" :value="option.value">{{ option.text }}</option>
                </select>
              </div>
              <button class="extend-button" @click="extendBooking(booking.id)">Extend</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import { useStore } from 'vuex';
  import LayoutHeader from '@/views/Layout/components/LayoutHeader.vue';
  import { getNearestScootersAPI } from "@/apis/scooter";
  import { Booking, getUserBookingsAPI, cancelBookingAPI, extendBookingAPI } from "@/apis/booking";
  
  // const map = ref();
  let map;
  const userMarker = ref(false);
  const nearbyScooters = ref([]); 
  const scooterMarkers = ref([]);
  const store = useStore();
  const myBookings = ref([]); // Store user's bookings
  const allScooters = ref([]); // Store all scooters
  const allScooterMarkers = ref([]); // Store all scooter markers
  const registeredUsers = ref([]); // Store registered users
  const isNewUser = ref(''); // Default to empty or 'new'
  const newUser = ref({
    name: '',
    email: '',
  });

  const getUserLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
      const userLocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      };
      fetchNearestScooters(userLocation); // 获取最近的滑板车
    }, error => {
      console.error('获取位置失败:', error.message);
      alert('无法获得用户位置，请检查设置。');
    });
  } else {
    alert("抱歉，您的浏览器不支持地理定位。");
  }
  };

  const fetchNearestScooters = async (userLocation) => {
  try {
    const { lat, lng } = userLocation; // Extract latitude and longitude from userLocation
    const radius = 50000000000; // Search radius in meters
    const response = await getNearestScootersAPI(lat, lng, radius);
    // nearbyScooters.value = response;

    // Compute distances and add them to the scooter objects
    nearbyScooters.value = response.map(scooter => {
    const distance = google.maps.geometry.spherical.computeDistanceBetween(
      new google.maps.LatLng(lat, lng),
      new google.maps.LatLng(scooter.locationLat, scooter.locationLng)
    ) / 1000; // Convert to kilometers
    return {
      ...scooter,
      distance: distance.toFixed(2), // Add distance property
    };
    });

    // Sort scooters by distance
    nearbyScooters.value.sort((a, b) => a.distance - b.distance);

    console.log("最近的滑板车:", nearbyScooters.value);

    // Clear existing scooter markers
    scooterMarkers.value.forEach(marker => marker.setMap(null));
    scooterMarkers.value = [];

    // Add new markers for the fetched scooters
    nearbyScooters.value.forEach(scooter => {
      const marker = new google.maps.Marker({
        position: { lat: scooter.locationLat, lng: scooter.locationLng },
        map: map.value,
        title: `滑板车ID: ${scooter.id}`,
        icon: {
          path: google.maps.SymbolPath.CIRCLE,
          scale: 5,
          fillColor: 'green',
          fillOpacity: 1,
          strokeWeight: 1,
        },
      });

      
      // Compute the nearest scooter from the scooter list
      const distance = google.maps.geometry.spherical.computeDistanceBetween(
        new google.maps.LatLng(lat, lng),
        new google.maps.LatLng(scooter.locationLat, scooter.locationLng)
      ) / 1000; // Convert to kilometers
      scooter.distance = distance.toFixed(2); // Add distance to the scooter object

      // Create an InfoWindow for the marker
      const infoWindow = new google.maps.InfoWindow({
        content: `
          <div>
            <h3>滑板车ID: ${scooter.id}</h3>
            <p>状态: ${scooter.status}</p>
            <p>距离: ${scooter.distance} km</p>
          </div>
        `,
      });

      // Add a click event listener to open the InfoWindow when the marker is clicked
      marker.addListener('click', () => {
        infoWindow.open(map.value, marker);
      });

      marker.setMap(map.value); 
      scooterMarkers.value.push(marker);
      console.log(`滑板车ID: ${scooter.id} - 距离: ${scooter.distance} km`);
      // center map to the nearest scooter
      if (nearbyScooters.value.length > 0) {
      const nearestScooter = nearbyScooters.value[0];
      map.value.setCenter({
        lat: nearestScooter.locationLat,
        lng: nearestScooter.locationLng,
      });
      map.value.setZoom(14); // Zoom in on the nearest scooter
      }
    });
  } catch (error) {
    console.error("获取最近的滑板车失败:", error.response?.data || error);
  }
};

  const initMap = () => {
    // 初始化地图
    map.value = new google.maps.Map(document.getElementById('map'), {
      center: { lat: 34.0522, lng: -118.2437 }, // 默认中心
      zoom: 14,
      mapId: "SCOOTER_MAP",
    });
  };
  

const centerToUserLocation = () => {
  if(!map.value){
    console.error("地图未初始化，请稍后再试。");
    return;
  }

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
      const userLocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      };

      if (userMarker.value) {
        userMarker.value.setMap(null); // 移除旧的标记
      }

      userMarker.value = new google.maps.Marker({
        position: userLocation,
        map: map.value,
        title: "您的位置",
        icon: {
          path: google.maps.SymbolPath.CIRCLE,
          scale: 7,
          fillColor: 'blue',
          fillOpacity: 1,
          strokeWeight: 1
        }
      });


      // User infoWindow
      const userInfoWindow = new google.maps.InfoWindow({
        content: `<div><strong>您的位置${userLocation.lat} ${userLocation.lng}</strong></div>`,
      });
      // open the info window when the marker is clicked
      userMarker.value.addListener('click', () => {
        userInfoWindow.open(map.value, userMarker.value);
      });
      map.value.setCenter(userLocation); // 设置地图中心为用户位置
      map.value.setZoom(15); // 放大地图
    }, error => {
      console.error('获取位置失败:', error.message);
      alert('无法获得用户位置，请检查设置。');
    });
  } else {
    alert("抱歉，您的浏览器不支持地理定位。");
  }
};

  
  onMounted( async() => {
    initMap();
  });
  
  // 预订功能
  const cardNumber = ref('');
  const scooterId = ref('');
  const selectedDuration = ref('1小时');
  const bookings = ref([]);
  // const options = ['1 hour', '4 hour', '1 day', '1 week'];
  // key value pair for options
  const options = [
    { text: '1 hour', value: 1 },
    { text: '4 hour', value: 4 },
    { text: '1 day', value: 24 },
    { text: '1 week', value: 168 },
  ];
  const prices = {
    '1 hour': 10,
    '4 hour': 30,
    '1 day': 50,
    '1 week': 200,
  };
  
  const bookScooter = async () => {
    if (!scooterId.value) {
      alert('请输入滑板车ID');
      return;
    }
    console.log('Booking scooter with ID:', scooterId.value, 'for duration:', selectedDuration.value);
    const userId = store.getters.user?.id; // Get the user ID from Vuex store
    console.log('User ID:', userId);
    const response = await Booking(userId, scooterId.value, selectedDuration.value, cardNumber.value);
    if (response) {
      bookings.value.push(response);
      scooterId.value = ''; // Clear the input field after booking
      selectedDuration.value = '1 hour'; // Reset to default duration
    } else {
      alert('if booking failed, please try again later'); 
    }
  };
  
  
  const cancelBooking = async (bookingId) => {
    console.log('Canceling booking with ID:', bookingId);
    try {
      const response = await cancelBookingAPI(bookingId); // Call the API to cancel the booking
      console.log('Cancel booking response:', response);
      if (response) {
        myBookings.value = myBookings.value.filter(booking => booking.id !== bookingId); // Remove the canceled booking from the list
        alert('Booking canceled successfully!');
      } else {
        alert('Failed to cancel booking, please try again later.');
      }
    } catch (error) {
      console.error('Cancel booking failed:', error.response?.data || error.message);
      alert('Cancel booking failed, please try again later.');
    }
  };

  const getMyBookings = async () => {
    const userId = store.getters.user?.id; // 从 Vuex 获取用户 ID
    console.log('User ID:', userId);

    try {
        const response = await getUserBookingsAPI(userId); // 调用 API 获取预订信息
        console.log('My bookings:', response);

        if (response) {
            // 遍历预订信息，计算价格并打印开始时间和结束时间
            myBookings.value = response.map(booking => {
                const startTime = new Date(booking.start_time); // 转换为 Date 对象
                const endTime = new Date(booking.end_time); // 转换为 Date 对象
                const durationInHours = (endTime - startTime) / (1000 * 60 * 60); // 计算时长（小时）

                // 根据时长计算价格
                let price = 0;
                if (durationInHours <= 1) {
                    price = 10;
                } else if (durationInHours <= 4) {
                    price = 30;
                } else if (durationInHours <= 24) {
                    price = 50;
                } else if (durationInHours <= 168) {
                    price = 200;
                }//超过一周在基础上以之前的价格计算
                else {
                    price = 200 + (durationInHours - 168) * 10; // 每小时10美元
                }


                console.log(`Booking ID: ${booking.id}`);
                console.log(`Start Time: ${startTime}`);
                console.log(`End Time: ${endTime}`);
                console.log(`Duration (hours): ${durationInHours}`);
                console.log(`Price: $${price}`);

                // 返回扩展后的预订对象
                return {
                    ...booking,
                    startTime,
                    endTime,
                    durationInHours,
                    price,
                };
            });
        } else {
            alert('获取我的预订失败，请稍后再试。');
        }
    } catch (error) {
        console.error('获取预订信息失败:', error.response?.data || error.message);
        alert('获取预订信息失败，请稍后再试。');
    }
};

const extendBooking = async (bookingId) => {
  console.log('Extending booking with ID:', bookingId);
  try {
    const response = await extendBookingAPI(bookingId, selectedDuration.value); // Call the API to extend the booking
    console.log('Extend booking response:', response);
    if (response) {
      myBookings.value = myBookings.value.map(booking => {
        if (booking.id === bookingId) {
          return { ...booking, duration: selectedDuration.value }; // Update the duration in the booking list
        }
        return booking;
      });
      alert('Booking extended successfully!');
    } else {
      alert('Failed to extend booking, please try again later.');
    }
  } catch (error) {
    console.error('Extend booking failed:', error.response?.data || error.message);
    alert('Extend booking failed, please try again later.');
  }
};
  </script>
  
  <style scoped>
  .dashboard-container {
    padding: 20px;
    background-color: #f8f9fa;
  }
  
  .title {
    text-align: center;
    margin-bottom: 30px;
  }
  
  .section {
    margin-bottom: 40px;
    padding: 20px;
    border-radius: 12px;
    background-color: #ffffff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    margin-bottom: 20px;
    color: #333;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  th,
  td {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: left;
  }
  
  th {
    background-color: #007bff;
    color: white;
  }
  
  .input-container {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  
  .input-field {
    flex: 1;
    margin-right: 10px;
  }
  
  .input-field label {
    display: block;
    margin-bottom: 5px;
  }
  
  .input-field input,
  .input-field select {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    transition: border-color 0.3s;
  }
  
  .book-button {
    background-color: #28a745;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .book-button:hover {
    background-color: #218838;
  }
  
  .booking-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  
  .booking-item {
    padding: 10px;
    border: 1px solid #ccc;
    margin-bottom: 10px;
    border-radius: 5px;
    background-color: #f7f7f7;
    display: flex;
    justify-content: space-between;
    align-items: center;
    transition: background-color 0.3s;
  }
  
  .cancel-button {
    background-color: #dc3545;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .cancel-button:hover {
    background-color: #c82333;
  }
  
  .scooter-item {
    display: flex;
    justify-content: space-between;
    margin: 5px 0;
  }
  
  button {
    cursor: pointer;
  }

  .centerToUser{
    background-color: #007bff;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    margin: 10px 20px;
  }

  .book-button {
    background-color: #28a745;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .my-bookings-section {
  margin-top: 20px;
}

.booking-item.even-row {
  background-color: #aec5d4; /* 浅灰色背景 */
}

.booking-item.odd-row {
  background-color: #b8a486; /* 白色背景 */
}

.booking-item:hover {
  background-color: #f1f1f1;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.booking-info {
  flex: 1;
}

.booking-info p {
  margin: 5px 0;
  color: #333;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cancel-button:hover {
  background-color: #c82333;
}

.getUserLocation {
  background-color: #077f0d;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin: 10px 20px;
}

.extend-button {
  background-color: #007bff;
  color: white;
  padding: 8px 12px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

</style>