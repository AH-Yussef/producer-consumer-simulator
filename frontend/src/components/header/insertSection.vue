<template>
  <div id="insert">
    <div id="queue-btn" class="insert-btn" @click="addQueue()">
      <div id="queue" class="icon">
        <div class="icon-label">Q</div>
      </div>
      <div class="label">Queue</div>
    </div>
    <div id="machine-btn" class="insert-btn" @click="addMachine()">
      <div id="machine" class="icon">
        <div class="icon-label">M</div>
      </div>
      <div class="label">Machine</div>
    </div>
    <div id="cursor-btn" class="action-btn" @click="selectCursor()">
      <img src="../../assets/insert/cursor.png" width="17px">
    </div>
    <div id="delete-btn" class="action-btn" @click="deleteComponent()">
      <img src="../../assets/insert/delete.png" width="16px">
    </div>
  </div>
</template>

<script>
import {mapGetters, mapActions } from 'vuex';

export default {
  name: 'insert',
  data() {
    return {
      components: {
        queue: "queue",
        machine: "machine",
      }
    }
  },
  computed: mapGetters(['getCurrentComponent', 'getCurrSelectedComponent']),
  methods: {
    ...mapActions(['enableDrawingMode', 'disableDrawingMode', 'setComponentType', 'setCurrentComponent', 'resetCurrSelectedComponent']),
    addQueue() {
      this.enableDrawingMode();
      this.setComponentType(this.components.queue);
    },
    addMachine() {
      this.enableDrawingMode();
      this.setComponentType(this.components.machine);
    },
    selectCursor() {
      this.disableDrawingMode();
      const currentComponent = this.getCurrentComponent;
      if(currentComponent == null) return
      else currentComponent.remove();
      this.setCurrentComponent(null);
    },
    deleteComponent() {
      const currentSelectedComponent = this.getCurrSelectedComponent;
      if(currentSelectedComponent != null) currentSelectedComponent.remove();
      this.resetCurrSelectedComponent();
    }
  }
}
</script>

<style scoped>
#insert {
  height: 100%;
  display: grid;
  grid-template-rows: repeat(2, 2.5rem);
  grid-template-columns: repeat(2, 5rem) 2.4rem;
  justify-items: center;
  align-items: center;
  justify-content: center;
  align-content: center;
}

.icon {
  border: 1px solid #212F3D;
  grid-row: 1 / 2;
  grid-column: 1/2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-label{
  height: 2rem;
  width: 2rem;
  text-align: center;
  vertical-align: middle;
  line-height: 2rem;
  font-size: x-large;
  font-weight: bolder;
  user-select: none;
}

.label {
  grid-column: 1 / 2;
  grid-row: 2 / 3;
  font-size: small;
  user-select: none;
}

.insert-btn {
  height: 100%;
  width: 5rem;
  display: grid;
  grid-template-rows: 4.2rem 0.7rem;
  grid-template-columns: 4rem;
  justify-items: center;
  align-items: center;
  justify-content: center;
  align-content: center;
  grid-row: 1 / 3;
}
.insert-btn:hover {
  background-color: #E5E7E9;
}

#queue-btn { 
  grid-column: 2 / 3;
}

#queue {
  height: 2rem;
  width: 3.2rem;
  background-color: #F4D03F;
}

#machine-btn { 
  grid-column: 1 / 2;
}
#machine {
  width: 3.2rem;
  height: 3.2rem;
  border-radius: 50%;
  background-color: #BB8FCE;
}

.action-btn {
  width: 2.5rem;
  height: 2.5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  grid-column: 3 / 4;
  user-select: none;
}
.action-btn:hover {
  background-color: #E5E7E9;
}

#cursor-btn {
  grid-row: 2 / 3;
}
#delete-btn {
  grid-row: 1 / 2;
}
</style>