import gsap from 'gsap';
import store from '../store';
import { AssemblyLineComponent } from './component.js';

export class Queue extends AssemblyLineComponent{
  static codeCount = 1;
  static availableCodes = [];

  shiftX = -1.6 * 16;
  shiftY = -1 * 16;

  constructor(x, y) {
    super(x, y);
    this.code = this._generateCode();
    this.isMachine = false;
    this.isStartingQueue = false;
    this._create();
    this._addEventListeners();
  }

  _create() {
    const board = document.getElementById("board");
    const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
    const newQueue = document.createElementNS(svgns, "rect");
    gsap.set(newQueue, {
      attr: {
        x: this.center.x + this.shiftX, y: this.center.y + this.shiftY,
        width: "3.2rem", height: "2rem",
        fill: "#F4D03F", stroke: "black", 'stroke-width': "2px", 
      }
    });
    
    const newQueueLabel = document.createElementNS(svgns, "text");
    newQueueLabel.textContent = `Q${this.code}`;
    gsap.set(newQueueLabel, {
      attr: {
        x: this.center.x , y: this.center.y, "text-anchor": "middle", 
        fill: "black", "font-size": "medium", dy: "0.4rem"
      },
      css: {
        userSelect: 'none', pointerEvents: 'none',
      }
    })

    board.appendChild(newQueue);
    board.appendChild(newQueueLabel);
    this.component = newQueue;
    this.componentLabel = newQueueLabel;

    this._addConnectionPoints();
  }

  updatePos(x, y){
    this.updateCenter(x, y);
    this._updateConnectionPointsPos();

    this.component.setAttribute("x", this.center.x + this.shiftX);
    this.component.setAttribute("y", this.center.y + this.shiftY);

    this.componentLabel.setAttribute("x", this.center.x);
    this.componentLabel.setAttribute("y", this.center.y);
  }

  remove() {
    Queue.availableCodes.push(this.code);
    store.getters.queues.delete(this.code);
    this.removeSelf();
  }

  _generateCode() {
    if(Queue.availableCodes.length > 0) return Queue.availableCodes.pop();
    else return Queue.codeCount++;
  }

  _moveMouseDownHandling() {
    this.mouseDown = true;
    this._trackMovement();
  }

  _trackMovement(){
    const headerOffsetHeight = document.getElementById("header").offsetHeight;

    const tracker = setInterval(() => {
      if(!this.mouseDown || !this.mouseOnBoard()) {
        clearInterval(tracker);
      }
      else {
        const updatedX = window.mouseX;
        const updatedY = window.mouseY - headerOffsetHeight;
        this.updatePos(updatedX, updatedY);
        this._updateConnectorsPos();
      }
    }, 10); 
  }

  _addEventListeners() {
    this.component.onmousedown = () => {
      this._moveMouseDownHandling();
      this.setSelfAsSelected();
    }
    this.component.onmouseup = () => this._moveMouseUpHandling();
  }

  isEndQueue() {
    if(this.toConnectionPoint.outConnectors.size > 0) return false;
    return true;
  }
}