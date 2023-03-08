declare module '*.png';

declare module 'dva-model-extend' {
  import {Model} from 'dva-core-ts';
  export default function modelExtend(...models: Model[]): Model;
}
