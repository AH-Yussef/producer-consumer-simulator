<template>
  <div id="simulation">
    <div id="num-label">Number of products</div>
    <input type="number" id="products-input" placeholder="20" v-model="productsNumber">
    <div id="start-end-btn" :class="['sim-btn', setStartEndBtn()]" @click="TriggerSimulation()">{{startEndBtnText}}</div>
    <div id="replay-btn" :class="replayBtnStatus" @click="replay()">
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
      simulationFinished: false,
      replayBtnStatus: 'hidden',
      seconds: 1,
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
    showReplayBtn() {
      this.replayBtnStatus = 'sim-btn';
    },
    hideReplayBtn() {
      this.replayBtnStatus = 'hidden';
    },
    TriggerSimulation() {
      if(!this.isValidCircuit()) return;
      this.toggleStartEnd();
      if(!this.start) {
        const queuesJson = this.getQueuesInfo();
        const machinesJson = this.getMachinesInfo();
        const productsNumber = this.getProductsNumber();

        console.log(queuesJson);
        console.log(machinesJson);

        this.disableInput();
        document.getElementById("board").style.pointerEvents = 'none';

        this.startSimulation(machinesJson, queuesJson, productsNumber);
      }
      else {
        this.endSimulation();
      }
    },
    //requests helper methods
    startSimulation(machinesJson, queuesJson, productsNumber) {
      this.hideReplayBtn();
      this.resetAllMachines();
      this.resetAllQueues();
      this.simulationFinished = false;
      this.seconds = 0;

      axios.post('http://localhost:8085//startSimulation', null, 
        {params :{
          jsonMachines: machinesJson,
          jsonQueues: queuesJson,
          numberOfProducts: productsNumber,
        }})
        .then( () => {
          const tracker = setInterval(() => {
            if(this.simulationFinished) {
              this.showReplayBtn();
              clearInterval(tracker);
            }

            else {
              this.refreshCircuit();
              this.seconds ++;
            }
          }, 1000);
        })
        .catch( (error) => console.log(error));
    },
    //replay
    replay() {
      this.resetAllMachines();
      this.resetAllQueues();
      let currSecond = 1;

      this.disableInput();
      document.getElementById("board").style.pointerEvents = 'none';
      
      const replayTracker = setInterval(() => {
        if(currSecond > this.seconds) {
          this.enableInput();
          document.getElementById("board").style.pointerEvents = 'auto';
      
          clearInterval(replayTracker);
        }
        else {
          axios.get('http://localhost:8085//replay', 
          {params: {
            second: currSecond ++,
          }})
          .then( (response) => {
            this.updateCircuit(response.data);
          })
          .catch( (error) => console.log(error));
        }
      }, 1000);
    },
    isSimulationOver() {
      axios.get('http://localhost:8085//isSimulationFinished')
      .then( (response) => {
        const isSimulationFinished = response.data;
        if(isSimulationFinished) {
          this.toggleStartEnd();
          this.endSimulation();
        }
      })
      .catch( (error) => console.log(error));
    },
    refreshCircuit() {
      axios.get('http://localhost:8085//getCurrentImage')
      .then( (response) => {
        console.log(response.data);
        this.updateCircuit(response.data);

        this.isSimulationOver();
      })
      .catch( (error) => console.log(error));
    },
    updateCircuit(circuitInfo) {
      const machinesInfo = circuitInfo.machines;
      const queuesInfo = circuitInfo.queues;
      const currNumberOfProducts = circuitInfo.productsNum;
      this.updateMachines(machinesInfo);
      this.updateQueues(queuesInfo);
      this.updateNumberOfProducts(currNumberOfProducts);    
    },
    updateNumberOfProducts(numberOfProducts) {
      this.productsNumber = numberOfProducts;
    },
    endSimulation() {
      this.simulationFinished = true;
      document.getElementById("board").style.pointerEvents = 'auto';
      this.productsNumber = 20;
      this.enableInput();
    },
    resetAllMachines() {
      for(let machine of this.machines.values()){
        machine.resetColor();
      }
    },
    resetAllQueues() {
      for(let queue of this.queues.values()){
        queue.updateProductsNumber(0);
      }
    },
    //parsing machines info
    updateMachines(machinesInfo) {
      for(let machine of machinesInfo){
        const machineId = machine.id;
        const updatedMachineColor = machine.color;
        const currentMachine = this.machines.get(machineId);

        const oldColor = currentMachine.fillColor;
        currentMachine.updateFillColor(updatedMachineColor);
        if(oldColor != updatedMachineColor && oldColor != "rgb(187,143,206)") currentMachine.flash(oldColor)
      }
    },
    updateQueues(queuesInfo) {
      for(let queue of queuesInfo) {
        const queueId = queue.id;
        const queueSize = queue.numberOfProducts;
        const currentQueue = this.queues.get(queueId);
        currentQueue.updateProductsNumber(queueSize);
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

      if(this.productsNumber == "") {
        this.setErrorMsg(`products number cannot be empty`);
        this.getErrorCard.showErrorMsg();
        return false;
      }

      if(this.productsNumber == 0) {
        this.setErrorMsg(`products number cannot be 0`);
        this.getErrorCard.showErrorMsg();
        return false;
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
      return Math.trunc(+this.productsNumber);
    },
    disableInput() {
      document.getElementById("products-input").disabled = true;
    },
    enableInput() {
      document.getElementById("products-input").disabled = false;
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