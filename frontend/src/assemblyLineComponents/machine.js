import gsap from 'gsap';
import store from '../store';
import { AssemblyLineComponent } from './component.js';

export class Machine extends AssemblyLineComponent{
  static codeCount = 0;
  static availableCodes = [];

  constructor(x, y) {
    super(x, y);
    this.code = this._generateCode();
    this.isMachine = true;
    this.fillColor = "rgb(187,143,206)";
    this._create();
    this._addEventListeners();
  }

  _create() {
    const board = document.getElementById("board");
    const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
    const newMachine = document.createElementNS(svgns, "circle");
    gsap.set(newMachine, {
      attr: {
        cx: this.center.x, cy: this.center.y, r: "1.6rem",
        fill: this.fillColor, stroke: "black", 'stroke-width': "2px", 
      }
    });
    
    const newMachineLabel = document.createElementNS(svgns, "text");
    newMachineLabel.textContent = `M${this.code}`;
    gsap.set(newMachineLabel, {
      attr: {
        x: this.center.x, y: this.center.y, "text-anchor": "middle", 
        fill: "white", "font-size": "medium", dy: "0.4rem",
      },
      css: {
        userSelect: 'none', pointerEvents: 'none',
      }
    })

    board.appendChild(newMachine);
    board.appendChild(newMachineLabel);
    this.component = newMachine;
    this.componentLabel = newMachineLabel;

    this._addConnectionPoints();
  }

  updatePos(x, y){
    this.updateCenter(x, y);
    this._updateConnectionPointsPos();

    this.component.setAttribute("cx", this.center.x);
    this.component.setAttribute("cy", this.center.y);

    this.componentLabel.setAttribute("x", this.center.x);
    this.componentLabel.setAttribute("y", this.center.y);
  }

  updateFillColor(rgbColor) {
    this.fillColor = rgbColor;
    this.component.setAttribute("fill", rgbColor);
  }
  resetColor() {
    this.fillColor = "rgb(187,143,206)";
    this.component.setAttribute("fill", this.fillColor);
  }

  remove() {
    Machine.availableCodes.push(this.code);
    store.getters.machines.delete(this.code);
    this.removeSelf();
  }

  _generateCode() {
    if(Machine.availableCodes.length > 0) return Machine.availableCodes.pop();
    else return Machine.codeCount++;
  }

  //movement
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

  flash(rgbColor){
    const flasher = setInterval(() => {
      this.component.setAttribute("stroke", rgbColor);
      this.component.setAttribute("stroke-width", '8px');

      setTimeout(() => {
        this.component.setAttribute("stroke", "black");
        this.component.setAttribute("stroke-width", '2px');
      }, 150);
    }, 300);

    setTimeout(function( ) { 
      clearInterval( flasher ); 
    }, 1000);
  }
}