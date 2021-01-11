import { ConnectionPoint } from "./connectionPoint.js";
import { Connector } from "../connector.js";
import store from "../../store";

export class ToConnectionPoint extends ConnectionPoint{
  constructor(component) {
    super(component);
    this.tempConnector = null;
    this.outConnectors = new Map();

    this._createConnectionWrapper(component.center.y - ((component.width / 2)*16));
    this._createConnectionPoint(component.center.x - ((component.width / 2)*16));
    this._addActions();
  }

  //remove self and the connectors
  deleteAll() {
    this.removeSelf();
    for(let connector of this.outConnectors.values()) {
      connector.removeSelf();
    }
  }

  updatePos() {
    this.connectionPoint.setAttribute("cx", this.componentAttached.center.x - ((this.componentAttached.width / 2)*16));
    this.connectionPoint.setAttribute("cy", this.componentAttached.center.y);

    this.connectionPointWrapper.setAttribute("cx", this.componentAttached.center.x - ((this.componentAttached.width / 2)*16));
    this.connectionPointWrapper.setAttribute("cy", this.componentAttached.center.y);
  }

  _addActions() {
    this.connectionPoint.onmousedown = () => {
      this._addConnector();
    }
  }

  reset() {
    this.removeSelf();
    this._createConnectionWrapper(this.componentAttached.center.y - ((this.componentAttached.width / 2)*16));
    this._createConnectionPoint(this.componentAttached.center.x - ((this.componentAttached.width / 2)*16));
    this._addActions();
  }

  //initializing connector
  _addConnector() {
    if(this.componentAttached.isMachine && this.outConnectors.size > 0) return;

    const headerOffsetHeight = document.getElementById("header").offsetHeight;
    this.tempConnector = new Connector(this.componentAttached);
    store.dispatch('setFromComponent', this.componentAttached);
    
    const tracker = setInterval(() => {
      if(!store.getters.isBoardMouseDown) {
        if(store.getters.getToComponent == null){
          this._removeConnector();
        }
        else{
          this._hookConnector();
        }
        clearInterval(tracker);
      }
      else {
        const updatedX = window.mouseX;
        const updatedY = window.mouseY - headerOffsetHeight;
        this.tempConnector.updateEndingPoint(updatedX, updatedY);
      }
    }, 10); 
  }

  _removeConnector() {
    this.tempConnector.removeSelf();
  }

  _hookConnector() {
    const toComponent = store.getters.getToComponent;
    if(!this.componentAttached.isMachine && !toComponent.isMachine){
      this._removeConnector();
      return;
    }
    if(this.componentAttached.isMachine && toComponent.isMachine){
      this._removeConnector();
      return;
    }
    if(toComponent.fromConnectionPoint.hasFromComponent(this.componentAttached.code)){
      this._removeConnector();
      return;
    }

    this.tempConnector.setToComponent(toComponent);

    toComponent.fromConnectionPoint.addFromComponent(this.componentAttached);
    toComponent.fromConnectionPoint.addInConnector(this.tempConnector);
    
    this.outConnectors.set(this.tempConnector.code, this.tempConnector);
    this.tempConnector.createConnectorWrapper();

    this.componentAttached.resetConnectionPoints();
    toComponent.resetConnectionPoints();

    store.dispatch('resetFromComponent');
    store.dispatch('resetToComponent');
  }
}