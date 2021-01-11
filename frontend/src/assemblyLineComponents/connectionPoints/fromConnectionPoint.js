import { ConnectionPoint } from "./connectionPoint.js";
import store from '../../store';

export class FromConnectionPoint extends ConnectionPoint{
  constructor(component) {
    super(component);
    this.fromComponents = new Map();
    this.inConnectors = new Map();

    this._createConnectionWrapper(component.center.x + ((component.width / 2)*16));
    this._createConnectionPoint(component.center.x + ((component.width / 2)*16));
    this._addActions();
  }

  //remove self and the connectors
  deleteAll() {
    this.removeSelf();
    for(let connector of this.inConnectors.values()) {
      connector.removeSelf();
    }
  }

  updatePos() {
    this.connectionPoint.setAttribute("cx", this.componentAttached.center.x + ((this.componentAttached.width / 2)*16));
    this.connectionPoint.setAttribute("cy", this.componentAttached.center.y);

    this.connectionPointWrapper.setAttribute("cx", this.componentAttached.center.x + ((this.componentAttached.width / 2)*16));
    this.connectionPointWrapper.setAttribute("cy", this.componentAttached.center.y);
  }

  _addActions() {
    this.connectionPoint.onmouseover = () => {
      this._setAsToComponent();
    }
    
    this.connectionPointWrapper.onmouseleave = () => {
      this._removeAsToComponent();
    }

    this.connectionPointWrapper.onmouseover = () => {
      this._setAsToComponent();
    }
  }

  reset() {
    this.removeSelf();
    this._createConnectionWrapper(this.componentAttached.center.y + ((this.componentAttached.width / 2)*16));
    this._createConnectionPoint(this.componentAttached.center.x + ((this.componentAttached.width / 2)*16));
    this._addActions();
  }

  //connection handling
  _setAsToComponent() {
    store.dispatch('setToComponent', this.componentAttached);
  }

  _removeAsToComponent() {
    store.dispatch('resetToComponent');
  }

  addFromComponent(fromComponent) {
    this.fromComponents.set(fromComponent.code, fromComponent);
  }

  addInConnector(connector) {
    this.inConnectors.set(connector.code, connector);
  }

  hasFromComponent(code) {
    return this.fromComponents.has(code);
  }
}