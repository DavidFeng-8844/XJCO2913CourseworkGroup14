<template>

        <div class="card-form">
            <!-- 卡片预览区域 -->
            <div class="card-list">
                <div class="card-item" :class="{ '-active': isCardFlipped }">
                    <!-- 卡片正面 -->
                    <div class="card-item__side -front">
                        <div class="card-item__focus" :class="{ '-active': !!focusElementStyle }"
                            :style="focusElementStyle" ref="focusElement"></div>
                        <div class="card-item__cover">
                            <img :src="cardBackgroundUrl" class="card-item__bg" />
                        </div>
                        <div class="card-item__wrapper">
                            <div class="card-item__top">
                                <img :src="chipImage" class="card-item__chip" />
                                <div class="card-item__type">
                                    <transition name="slide-fade-up">
                                        <img :key="getCardType" v-if="getCardType" :src="cardTypeImageUrl"
                                            class="card-item__typeImg" />
                                    </transition>
                                </div>

                            </div>
                            <label class="card-item__number" ref="cardNumberRef">
                                <span v-for="(n, $index) in cardMask" :key="$index">
                                    <transition name="slide-fade-up">
                                        <span v-if="maskedCardNumber[$index]">{{ maskedCardNumber[$index] }}</span>
                                        <span v-else>{{ n }}</span>
                                    </transition>
                                </span>
                            </label>
                            <div class="card-item__content">
                                <label class="card-item__info" ref="cardNameRef">
                                    <div class="card-item__holder">Card Holder</div>
                                    <transition name="slide-fade-up">
                                        <div class="card-item__name" v-if="cardName">
                                            <transition-group name="slide-fade-right">
                                                <span class="card-item__nameItem"
                                                    v-for="(n, $index) in formattedCardName" :key="$index">{{ n
                                                    }}</span>
                                            </transition-group>
                                        </div>
                                        <div class="card-item__name" v-else>FULL NAME</div>
                                    </transition>
                                </label>
                                <div class="card-item__date" ref="cardDateRef">
                                    <label class="card-item__dateTitle">Expires</label>
                                    <label class="card-item__dateItem">{{ cardMonth || 'MM' }}</label>
                                    /
                                    <label class="card-item__dateItem">{{ cardYear ? String(cardYear).slice(2) : 'YY'
                                        }}</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 卡片背面 -->
                    <div class="card-item__side -back">
                        <div class="card-item__cover">
                            <img :src="cardBackgroundUrl" class="card-item__bg" />
                        </div>
                        <div class="card-item__band"></div>
                        <div class="card-item__cvv">
                            <div class="card-item__cvvTitle">CVV</div>
                            <div class="card-item__cvvBand">
                                <span v-for="(n, i) in cardCvv" :key="i">*</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 表单输入 -->
            <div class="card-form__inner">
                <div class="card-input">
                    <label class="card-input__label">Card Number</label>
                    <input class="card-input__input" v-model="cardNumber" @input="formatCardNumber"
                        @focus="focusInput('cardNumberRef')" @blur="clearFocus" ref="inputCardNumber" />
                </div>
                <div class="card-input">
                    <label class="card-input__label">Card Holder</label>
                    <input class="card-input__input" v-model="cardName" @focus="focusInput('cardNameRef')"
                        @blur="clearFocus" />
                </div>
                <div class="card-form__row">
                    <div class="card-form__col">
                        <label class="card-input__label">Month</label>
                        <select class="card-input__input -select" v-model="cardMonth" @focus="focusInput('cardDateRef')"
                            @blur="clearFocus">
                            <option disabled value="">MM</option>
                            <option v-for="n in 12" :value="n < 10 ? '0' + n : '' + n" :key="n">{{ n }}</option>
                        </select>
                    </div>
                    <div class="card-form__col">
                        <label class="card-input__label">Year</label>
                        <select class="card-input__input -select" v-model="cardYear" @focus="focusInput('cardDateRef')"
                            @blur="clearFocus">
                            <option disabled value="">YY</option>
                            <option v-for="n in 10" :value="currentYear + n" :key="n">{{ currentYear + n }}</option>
                        </select>
                    </div>
                    <div class="card-form__col -cvv">
                        <label class="card-input__label">CVV</label>
                        <input class="card-input__input" type="text" maxlength="4" v-model="cardCvv"
                            @focus="flipCard(true); focusInput('cardDateRef')" @blur="flipCard(false); clearFocus()" />
                    </div>
                </div>
                <button class="card-form__button">
                    Submit
                </button>
            </div>
            <!-- 提交按钮 -->


        </div>

</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const cardNumber = ref('')
const cardName = ref('')
const cardMonth = ref('')
const cardYear = ref('')
const cardCvv = ref('')
const currentYear = new Date().getFullYear()
const isCardFlipped = ref(false)
const focusElementStyle = ref(null)

const cardNumberRef = ref(null)
const cardNameRef = ref(null)
const cardDateRef = ref(null)
const focusElement = ref(null)
const inputCardNumber = ref(null)

onMounted(() => {
    inputCardNumber.value?.focus()
})

const getCardType = computed(() => {
    const number = cardNumber.value.replace(/\s+/g, '')
    if (/^4/.test(number)) return 'visa'
    if (/^(34|37)/.test(number)) return 'amex'
    if (/^5[1-5]/.test(number)) return 'mastercard'
    if (/^6011/.test(number)) return 'discover'
    if (/^9792/.test(number)) return 'troy'
    return 'visa'
})

const cardMask = computed(() =>
    getCardType.value === 'amex'
        ? '#### ###### #####'.split('')
        : '#### #### #### ####'.split('')
)

const formattedCardName = computed(() => cardName.value.trim().split(''))

const maskedCardNumber = computed(() => {
    const raw = cardNumber.value.replace(/\D/g, '')
    const mask = cardMask.value
    const result = []
    let rawIndex = 0
    for (let i = 0; i < mask.length; i++) {
        if (mask[i] === ' ') {
            result.push(' ')
        } else if (rawIndex < raw.length) {
            if (rawIndex >= 4 && rawIndex < 12) {
                result.push('*')
            } else {
                result.push(raw[rawIndex])
            }
            rawIndex++
        } else {
            result.push('')
        }
    }
    return result
})

const currentCardBackground = ref(Math.floor(Math.random() * 25 + 1))

const chipImage = new URL('@/assets/images/creditcard/chip.png', import.meta.url).href

const cardBackgroundUrl = computed(() =>
    new URL(`../../../assets/images/creditcard/${currentCardBackground.value}.jpeg`, import.meta.url).href
)

const cardTypeImageUrl = computed(() =>
    new URL(`../../../assets/images/creditcard/${getCardType.value}.png`, import.meta.url).href
)

function flipCard(status) {
    isCardFlipped.value = status
}

function focusInput(refName) {
    const targetRef = {
        cardNumberRef,
        cardNameRef,
        cardDateRef
    }[refName]
    const target = targetRef?.value
    if (!target) return
    const rect = target.getBoundingClientRect()
    focusElementStyle.value = {
        width: rect.width + 'px',
        height: rect.height + 'px',
        transform: `translateX(${target.offsetLeft}px) translateY(${target.offsetTop}px)`
    }
}


function formatCardNumber() {
    let raw = cardNumber.value.replace(/\D/g, '')
    const mask = getCardType.value === 'amex' ? [4, 6, 5] : [4, 4, 4, 4]
    let formatted = ''
    let index = 0
    for (const group of mask) {
        if (raw.length > index) {
            formatted += raw.substring(index, index + group) + ' '
            index += group
        }
    }
    cardNumber.value = formatted.trim()
}
</script>


<style scoped>
@import '../../../styles/wallet.scss';

.slide-fade-up-enter-active,
.slide-fade-up-leave-active {
    transition: all 1s cubic-bezier(0.25, 0.8, 0.5, 1);
}

.slide-fade-up-enter-from,
.slide-fade-up-leave-to {
    opacity: 0;
    transform: translateY(-30px);
    /* 更远一点的滑动 */
}


.slide-fade-right-enter-active,
.slide-fade-right-leave-active {
    transition: all 0.3s ease;
}

.slide-fade-right-enter-from,
.slide-fade-right-leave-to {
    opacity: 0;
    transform: translateX(10px);
}


</style>