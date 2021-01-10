import Vuex from 'vuex';
import Vue from 'vue';
import construction from './modules/construction.js';
import connection from './modules/connection.js';

//load Vuex
Vue.use(Vuex);

//Create the store
export default new Vuex.Store({
  modules: {
    construction,
    connection,
  }
});