<template>
  <div id="error-msg" class="hidden">
    <div id="label">!</div>
    <div id="msg">{{getErrorMsg}}</div>

  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'errorMsg',
  data() {
    return {

    }
  },
  computed: mapGetters(['getErrorMsg', 'getErrorCard']),
  methods: {
    ...mapActions(['setErrorCard']),
    showErrorMsg() {
      const errorMsgCard = document.getElementById("error-msg");
      errorMsgCard.className = "visible";

      setTimeout(() => this.hideErrorMsg(), 4000);
    },

    hideErrorMsg() {
      const errorMsgCard = document.getElementById("error-msg");
      errorMsgCard.className = "hidden";
    }
  },
  mounted() {
    this.setErrorCard(this);
  }
}
</script>

<style scoped>
#error-msg {
  position: absolute;
  height: 2rem;
  top: 6rem;
  left: 1rem;
  z-index: 100;

  display: flex;
  justify-content: flex-start;
  align-items: center;

  padding-left: 0.25rem;

  background-color: #E74C3C;

  border-radius: 1rem;
  color: white;
  font-weight: bold;
  font-size: medium;
}

#label {
  height: 1.5rem;
  width: 1.5rem;
  border-radius: 50%;
  background-color: white;
  color: #E74C3C;
  text-align: center;
  vertical-align: middle;
  line-height: 1.5rem;
  font-size: larger;
  font-weight: bolder;
}

#msg {
  margin: 1rem;
  height: 1.7rem;
  text-align: left;
  vertical-align: middle;
  line-height: 1.7rem;
  color: white;
}

.visible {
  visibility: visible;
  display: flex;
  transition: 0.3s linear;
}

.hidden {
  visibility: hidden;
  display: none;
  transition: 0.3s linear;
}
</style>