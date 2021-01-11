import gsap from 'gsap';
import store from '../store';

export class Connector {
    static codeCount = 1;

    constructor(fromComponent) {
      this.code = this._generateCode();
      this.connector = null;
      this.wrapper = null;

      this.startingPoint = { x: 0, y: 0};
      this.endingPoint = { x: 0, y: 0};

      this.from = fromComponent;
      this.to = null;

      this.mouseDown = false;

      this._create();
    }

    _create() {    
      const board = document.getElementById("board");
      const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
      const newConnector = document.createElementNS(svgns, "line");

      this.startingPoint.x = this.from.center.x - ((this.from.width / 2)*16);
      this.startingPoint.y = this.from.center.y;
      this.endingPoint.x = this.from.center.x - ((this.from.width / 2)*16);
      this.endingPoint.y = this.from.center.y;

      gsap.set(newConnector, {
        attr: {
          x1: this.startingPoint.x, y1: this.startingPoint.y, //startint point
          x2: this.endingPoint.x, y2: this.endingPoint.y, //ending point
          stroke: "black", 'stroke-width': "2px",
        },
        css: {
          pointerEvents: "none",
        }
      });
      board.appendChild(newConnector);

      this.connector = newConnector;
    }

    updateStartingPoint() {
      this.startingPoint.x = this.from.center.x - ((this.from.width / 2)*16);
      this.startingPoint.y = this.from.center.y;

      this.connector.setAttribute("x1", this.startingPoint.x);
      this.connector.setAttribute("y1", this.startingPoint.y);

      this._setWrapper();
    }

    updateEndingPoint(x, y) {
      this.endingPoint.x = x;
      this.endingPoint.y = y;
      
      this.connector.setAttribute("x2", x);
      this.connector.setAttribute("y2", y);

      this._setWrapper();
    }

    updateEndingPointAuto() {
      this.updateEndingPoint(this.to.center.x + ((this.to.width / 2)*16), this.to.center.y);
    }

    setToComponent(toComponent) {
      this.to = toComponent;
      const toPointX = toComponent.center.x + ((toComponent.width / 2)*16);
      const toPointY = toComponent.center.y;
      this.updateEndingPoint(toPointX, toPointY);
    }

    removeSelf(){
      this.connector.remove();
      if(this.to != null) this.to.fromConnectionPoint.fromComponents.delete(this.from.code);
      if(this.wrapper != null) this.wrapper.remove();
    }

    //for interface compatiblity regards
    remove() {
      this.removeSelf();
    }

    _generateCode() {
      return Connector.codeCount++;
    }

    //connector wrapper
    createConnectorWrapper(){
      const board = document.getElementById("board");
      const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
      const selectionWrapper = document.createElementNS(svgns, "ellipse");
      this.wrapper = selectionWrapper;

      this._setWrapper();

      this.wrapper.onmousedown = () => {
        this.setSelfAsSelected();
        this.mouseDown = true;
      }

      this.wrapper.onmouseup = () => this.mouseDown = false;
      
      board.appendChild(selectionWrapper);
    }

    _setWrapper(){
      if(this.wrapper == null) return;
      
      const lineLength = Math.sqrt(Math.pow((this.endingPoint.x - this.startingPoint.x), 2) +
      Math.pow((this.endingPoint.y - this.startingPoint.y), 2));
  
      const centerX = (this.startingPoint.x + this.endingPoint.x) /2;
      const centerY = (this.startingPoint.y + this.endingPoint.y) /2;
  
      const width = this.endingPoint.x - this.startingPoint.x;
      const height = this.endingPoint.y - this.startingPoint.y;

      const rotationAngle = Math.atan(height/ width) *(180/ Math.PI);
  
      const selectorRadiusW = (lineLength/2) - 5;
      const selectorRadiusH = 5;
  
      gsap.set(this.wrapper, {
        attr: {
          cx: centerX, cy: centerY, rx: selectorRadiusW, ry: selectorRadiusH,
          fill: 'transparent',
          transform: `rotate(${rotationAngle} ${centerX} ${centerY})`,
        }
      });
    }

    //selection
    setSelfAsSelected() {
      const prevSelectedComponent = store.getters.getCurrSelectedComponent;
      if(prevSelectedComponent != null) {
        prevSelectedComponent.unselectSelf();
      }
  
      store.commit('setCurrSelectedComponent', this);
      this._selectSelf();
    }
  
    _selectSelf() {
      this.connector.setAttribute("stroke", "#E74C3C");
    }

    unselectSelf() {
      this.connector.setAttribute("stroke", "black");
    }
}












