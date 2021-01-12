<template>
  <svg  id="board" :width="boardProbs.width" :height="boardProbs.height"
        version="1.1" xmlns="http://www.w3.org/2000/svg"
        @mouseover="mouseOverHandling()"
        @mousedown="mouseDownHandling()"
        @mouseup="mouseUpHandling()">
  </svg>
</template>

<script>
import { mapGetters, mapActions} from 'vuex';
import { Machine } from '../assemblyLineComponents/machine.js';
import { Queue } from '../assemblyLineComponents/queue.js';
import { StartingQueue } from '../assemblyLineComponents/startingQueue.js';

export default {
  name: 'board',
  data() {
    return {
      boardProbs: {
        width: 100,
        height: 100,
      },
      components: {
        queue: "queue",
        machine: "machine",
      },
      currentComponent: {
        component: null,
        startingPos: { x: 0, y: 0 },
      },
      mousePointer: {
        originX: 0,
        originY: 0,
        x: 0, //current position relative to x-axis
        y: 0, //current position relative to y-axis
        updatePosition: function() {
          this.x = window.mouseX - this.originX;
          this.y = window.mouseY - this.originY;
        },
      }
    }
  },
  computed: mapGetters(['isDrawing', 'componentType', 'getCurrentComponent', 'getCurrSelectedComponent', 'queues', 'machines']),
  methods: {
    ...mapActions(['disableDrawingMode', 'pushNewQueue', 'pushNewMachine', 'setCurrentComponent', 'setBoardMouseDown']),
    //Drawing Shapes
    mouseOverHandling(){
      const mousePosTracker = setInterval(() => {
        if(!this.isDrawing || !this.mouseOnBoard()) clearInterval(mousePosTracker);
        else {
          this.mousePointer.updatePosition();

          if(this.getCurrentComponent == null){
            this.setStartingPos();
            this.createComponent();
            this.setCurrentComponent(this.currentComponent.component);
          }

          const updatedX = this.mousePointer.x;
          const updatedY = this.mousePointer.y;
          this.currentComponent.component.updatePos(updatedX, updatedY);
        }
      }, 10); 
    },

    mouseDownHandling(){      
      this.setBoardMouseDown(true);
      const currSelectedComponent =  this.getCurrSelectedComponent;
      if(currSelectedComponent != null && !currSelectedComponent.mouseDown) currSelectedComponent.unselectSelf();

      this.resetAllMachines();
      this.resetAllQueues();
      
      if(this.getCurrentComponent == null) return;

      if(this.isDrawing) {
        if(this.isQueue()) this.pushNewQueue(this.currentComponent.component);
        else if(this.isMachine()) this.pushNewMachine(this.currentComponent.component);
        this.disableDrawingMode();
        this.setCurrentComponent(null);
      }
    },

    mouseUpHandling() {
      this.setBoardMouseDown(false);
    },

    createComponent(){
      if (this.isQueue()){
        this.currentComponent.component = new Queue(this.currentComponent.startingPos.x, this.currentComponent.startingPos.y);
      }
      else if(this.isMachine()){
        this.currentComponent.component = new Machine(this.currentComponent.startingPos.x, this.currentComponent.startingPos.y);
      }
    },
    setStartingPos(){
      this.currentComponent.startingPos.x = this.mousePointer.x;
      this.currentComponent.startingPos.y = this.mousePointer.y;
    },
    mouseOnBoard(){
      return this.board.matches(':hover');
    },
    isQueue(){
      return this.componentType === this.components.queue;
    },
    isMachine(){
      return this.componentType === this.components.machine;
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
    //initializing the circuit
    mountStartingQueue() {
      const startingQueueX = this.boardProbs.width - 70;
      const startingQueueY = this.boardProbs.height / 2;
      const startingQueue = new StartingQueue(startingQueueX, startingQueueY);
      this.pushNewQueue(startingQueue);
    }
  },
  mounted(){
    //setting the height and the width of the svg board
    this.board = document.getElementById("board");
    this.boardProbs.width = window.innerWidth;
    const headerHeight = document.getElementById("header").offsetHeight;
    this.mousePointer.originY = headerHeight;
    this.boardProbs.height = window.innerHeight - headerHeight;

    this.mountStartingQueue();

    document.onmousemove = function(e) {
      var event = e || window.event;
      window.mouseX = event.clientX;
      window.mouseY = event.clientY;
    }
  }
}
</script>

<style scoped>
#board{
  margin-top: 1px;
  background-color: white;
  position: relative;
  z-index: 1;
}
</style>