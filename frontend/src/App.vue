<template>
  <div id="app">
    <appHeader/>
    <errorMsg/>
    <board/>
  </div>
</template>

<script>
import appHeader from "./components/header.vue";
import board from "./components/board.vue";
import errorMsg from "./components/errorMsg.vue";

export default {
  name: 'App',
  components: {
    appHeader,
    board,
    errorMsg,
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Comfortaa:wght@600&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  overflow: hidden;
}

html, body {
  height: 100%;
  width: 100%;
}


#app {
  height: 100%;
  width: 100%;
  font-family: 'Comfortaa', cursive;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
