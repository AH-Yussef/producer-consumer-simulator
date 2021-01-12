<template>
  <div id="clear-all">
    <div id="delete-all-btn" @click="clearAll()">clear all</div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'clearAll',
  data() {
    return {

    }
  },
  computed: mapGetters(['queues', 'machines', 'getErrorCard']),
  methods: {
    ...mapActions(['resetCurrSelectedComponent']),
    clearAll() {
      for(let machine of this.machines.values()){
        machine.remove();
      }

      for(let queue of this.queues.values()){
        queue.remove();
      }

      this.resetCurrSelectedComponent();
    }
  }
}
</script>

<style>
#clear-all {
  height: 100%;
  width: 10rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

#delete-all-btn {
  background-color: #E74C3C;
  border-radius: 0.5rem;
  height: 2rem;
  width: 6rem;

  text-align: center;
  vertical-align: center;
  line-height: 2rem;

  color: white;
  user-select: none;
}
</style>