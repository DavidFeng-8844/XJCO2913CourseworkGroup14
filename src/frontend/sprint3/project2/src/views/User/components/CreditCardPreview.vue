<template>
  <div class="credit-preview">
    <div class="card-preview-list">
      <div class="card-list" v-for="(card, index) in cards" :key="index">
        <div class="card-item -active">
          <!-- 卡片正面 -->
          <div class="card-item__side -front">
            <div class="card-item__cover">
              <img :src="getBg(card.background)" class="card-item__bg" />
            </div>
            <div class="card-item__wrapper">
              <div class="card-item__top">
                <img :src="getChip()" class="card-item__chip" />
                <div class="card-item__type">
                  <img :src="getTypeImg(card.type)" class="card-item__typeImg" />
                </div>
              </div>
              <label class="card-item__number">
                <span>{{ card.number.slice(0, 4) }}</span>
                <span>****</span>
                <span>****</span>
                <span>{{ card.number.slice(-4) }}</span>
              </label>
              <div class="card-item__content">
                <label class="card-item__info">
                  <div class="card-item__holder">Card Holder</div>
                  <div class="card-item__name">{{ card.name }}</div>
                </label>
                <div class="card-item__date">
                  <label class="card-item__dateTitle">Expires</label>
                  <label class="card-item__dateItem">{{ card.month }}</label> /
                  <label class="card-item__dateItem">{{ card.year.slice(2) }}</label>
                </div>
              </div>
              <button class="card-item__delete" @click="removeCard(index)">×</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  cards: Array
})

const emit = defineEmits(['delete'])

function removeCard(index) {
  emit('delete', index)
}

function getBg(file) {
  return new URL(`../../../assets/images/creditcard/${file}`, import.meta.url).href
}

function getChip() {
  return new URL('../../../assets/images/creditcard/chip.png', import.meta.url).href
}

function getTypeImg(type) {
  return new URL(`../../../assets/images/creditcard/${type}.png`, import.meta.url).href
}
</script>

<style scoped>
.credit-preview {
  width: 100%;
}

.card-preview-list {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  justify-content: flex-start;
}

.card-list {
  width: calc(50% - 12px); /* 一排两张卡片，间距12px */
  display: flex;
  justify-content: center;
}

.card-item {
  width: 350px;
  position: relative;
}

.card-item__side {
  position: relative;
  width: 100%;
  height: 220px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  background: #eee;
}

.card-item__cover {
  width: 100%;
  height: 100%;
  position: relative;
}

.card-item__bg {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 黑色透明遮罩层 */
.card-item__cover::after {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.4); /* 半透明黑色 */
  z-index: 1;
}

.card-item__wrapper {
  position: absolute;
  inset: 0;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: white;
  font-family: 'Segoe UI', sans-serif;
  z-index: 2;
}

.card-item__top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-item__chip {
  width: 40px;
  height: auto;
}

.card-item__typeImg {
  height: 30px;
}

.card-item__number {
  font-size: 18px;
  letter-spacing: 2px;
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.card-item__holder,
.card-item__dateTitle {
  font-size: 10px;
  opacity: 0.8;
}

.card-item__name,
.card-item__dateItem {
  font-size: 14px;
  font-weight: bold;
}

.card-item__dateItem {
  margin-left: 3px;
  margin-top: 5px;
}


@media (max-width: 768px) {
  .card-list {
    width: 100%;
  }
}


.card-item__delete {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background-color: rgba(255, 255, 255, 0.15);
  border: none;
  color: white;
  font-size: 18px;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  cursor: pointer;
  transition: background 0.3s;
  z-index: 3;
}

.card-item__delete:hover {
  background-color: rgba(255, 0, 0, 0.5);
}

/* 一排两张卡片 */
.card-list {
  width: calc(50% - 12px);
}

@media (max-width: 600px) {
  /* 一排一张卡片 */
  .card-list {
    width: 100%;
  }
}




</style>
