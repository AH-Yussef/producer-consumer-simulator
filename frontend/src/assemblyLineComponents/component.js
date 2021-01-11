import store from "../store/index.js";
import { FromConnectionPoint } from "./connectionPoints/fromConnectionPoint.js";
import { ToConnectionPoint } from "./connectionPoints/toConnectionPoint.js";

export class AssemblyLineComponent {
  constructor(x, y) {
    this.component = null;
    this.componentLabel = null,
    this.center = { x: x, y: y };
    this.code = 0;
    this.width = 0;

    this.mouseDown = false;
    this.isMachine = false;

    this.fromConnectionPoint = null;
    this.toConnectionPoint = null;
  }

  updateCenter(x, y) {
    this.center.x = x;
    this.center.y = y;
  }

  removeSelf() {
    this.component.remove();
    this.componentLabel.remove();
    this.fromConnectionPoint.deleteAll();
    this.toConnectionPoint.deleteAll();
  }

  _moveMouseUpHandling() {
    this.mouseDown = false;
  }

  mouseOnBoard(){
    return document.getElementById("board").matches(':hover');
  }

  //connection points
  _addConnectionPoints() {
    this.fromConnectionPoint = new FromConnectionPoint(this);
    this.toConnectionPoint = new ToConnectionPoint(this);
  }

  _updateConnectionPointsPos() {
    this.fromConnectionPoint.updatePos();
    this.toConnectionPoint.updatePos();
  }

  _updateConnectorsPos() {
    for (let connector of this.fromConnectionPoint.inConnectors.values()) {
      connector.updateEndingPointAuto();
    }

    for (let connector of this.toConnectionPoint.outConnectors.values()) {
      connector.updateStartingPoint();
    }
  }

  resetConnectionPoints() {
    this.fromConnectionPoint.reset();
    this.toConnectionPoint.reset();
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
    this.component.setAttribute("stroke", "#E74C3C");
    this.component.setAttribute("stroke-width", "3px");
  }

  unselectSelf() {
    this.component.setAttribute("stroke", "black");
    this.component.setAttribute("stroke-width", "2px");
  }
}