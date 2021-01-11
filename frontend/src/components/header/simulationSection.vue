<template>
  <div id="simulation">
    <div id="num-label">Number of products</div>
    <input type="number" id="products-input" placeholder="20" v-model="productsNumber">
    <div id="start-end-btn" :class="['sim-btn', setStartEndBtn()]" @click="startSimulation()">{{startEndBtnText}}</div>
    <div id="replay-btn" :class="['sim-btn', 'hidden']">
      replay
    </div>
  </div>
</template>

<script>
import { mapGetters ,mapActions } from 'vuex';
import axios from 'axios';

export default {
  name: 'simulation',
  data() {
    return {
      start: true,
      productsNumber: 20,
      startEndBtnText: "start simulation",
    }
  },
  computed: mapGetters(['queues', 'machines', 'getErrorCard']),
  methods: {
    ...mapActions(['setErrorMsg']),
    toggleStartEnd() {
      this.start = !this.start;
      this.setStartEndBtn();
    },
    setStartEndBtn() {
      if(!this.start) {
        this.startEndBtnText = "end simulation";
        return "end-btn";
      }
      else {
        this.startEndBtnText = "start simulation";
        return "start-btn";
      }
    },
    startSimulation() {
      if(!this.isValidCircuit()) return;
      this.toggleStartEnd();
      if(!this.start) {
        const queuesJson = this.getQueuesInfo();
        const machinesJson = this.getMachinesInfo();
        const productsNumber = this.getProductsNumber();

        console.log(queuesJson);
        console.log(machinesJson);
        console.log(productsNumber);
        document.getElementById("board").style.pointerEvents = 'none';

        axios.post('http://localhost:8085//startSimulation', null, 
        {params :{
          jsonMachines: machinesJson,
          jsonQueues: queuesJson,
          numberOfProducts: productsNumber,
        }})
        .catch( (error) => console.log(error));
      }
      else {
        document.getElementById("board").style.pointerEvents = 'auto';
      }
    },
    isValidCircuit() {
      // any queue except the starting queue must have a least one input
      for(let queue of this.queues.values()) {
        if(!queue.isStartingQueue && queue.fromConnectionPoint.inConnectors.size == 0) {
          this.setErrorMsg(`Q${queue.code} has no input lines`);
          this.getErrorCard.showErrorMsg();
          return false;
        }
      }
      // any machine must have exactly one output and at least one input
      for(let machine of this.machines.values()) {
        if(machine.fromConnectionPoint.inConnectors.size == 0) {
          this.setErrorMsg(`M${machine.code} has no input lines`);
          this.getErrorCard.showErrorMsg();
          return false;
        }

        if(machine.toConnectionPoint.outConnectors.size == 0) {
          this.setErrorMsg(`M${machine.code} has no output lines`);
          this.getErrorCard.showErrorMsg();
          return false;
        }
      }

      return true;
    },
    getQueuesInfo() {
      const allQueuesInfo = [];
      for(let queue of this.queues.values()) {
        queue.unselectSelf();
        if(queue.isEndQueue()) allQueuesInfo.push(new this.QueueInfo(queue.code, true));
        else allQueuesInfo.push(new this.QueueInfo(queue.code, false));
      }

      return JSON.stringify(allQueuesInfo);
    },
    getMachinesInfo() {
      const allMahinesInfo = [];
      for(let machine of this.machines.values()) {
        machine.unselectSelf();
        const fromQueues = machine.fromConnectionPoint.fromComponents.values();
        const fromQueuesIds = [];
        for(let queue of fromQueues) fromQueuesIds.push(queue.code);

        const toQueue = machine.toConnectionPoint.outConnectors.values().next().value.to.code;
        allMahinesInfo.push(new this.MachinesInfo(machine.code, fromQueuesIds, toQueue))
      }

      return JSON.stringify(allMahinesInfo);
    },
    getProductsNumber() {
      if(this.productsNumber == "") this.productsNumber = 20;
      return Math.trunc(+this.productsNumber);
    },
    //helper
    QueueInfo(id, isEndQueue) {
      return {
        ID: id,
        isEndQueue: isEndQueue,
      }
    },
    MachinesInfo(id, fromQueues, toQueue) {
      return {
        ID: id,
        fromQueues: fromQueues,
        toQueue: toQueue,
      }
    },
  },
}
</script>

<style scoped>
#simulation {
  height: 100%;
  display: grid;
  grid-template-rows: repeat(2, 2.5rem);
  grid-template-columns: repeat(5, 4rem);
  justify-items: center;
  align-items: center;
  justify-content: center;
  align-content: center;
  margin-left: 3rem;
}
#num-label {
  height: 2.2rem;
  width: 90%;
  text-align: center;
  vertical-align: middle;
  line-height: 2.2rem;
  color: black;
  user-select: none;

  grid-row: 1 / 2;
  grid-column: 1 / 4;
}

#products-input {
  box-sizing: border-box;
  border: 1px solid black;
  border-radius: 0.5rem;
  width: 90%;
  height: 2rem;
  grid-row: 1 / 2;
  grid-column: 4 / 6;
  padding-left: 0.5rem;
  font-size: medium;
}
#products-input::-webkit-outer-spin-button,
#products-input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
  margin: 0 !important;
}

input[type=number]:focus {
  outline-style: none;
  border: 2px solid black;
  border-radius: 0.5rem;
}

.icon {
  border: 1px solid #212F3D;
  grid-row: 1 / 2;
  grid-column: 1/2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.sim-btn {
  height: 2rem;
  width: 80%;
  text-align: center;
  vertical-align: middle;
  line-height: 2rem;
  user-select: none;
  border-radius: 0.5rem;
  grid-row: 2 / 3;
}

#start-end-btn { 
  grid-column: 1 / 4;
}

.start-btn {
  background-color: #16A085;
}

.end-btn {
  background-color: #E74C3C;
}

.hidden {
  visibility: hidden;
  display: none;
}
.visible {
  visibility: visible;
  display: flex;
}

#replay-btn { 
  background-color: #F4D03F;
  grid-column: 4 / 6;
}
</style>