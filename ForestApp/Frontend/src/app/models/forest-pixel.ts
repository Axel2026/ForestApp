import { ForestFireState } from './forest-fire-state';
export interface IForestPixel {

    temparature: number;
    humidity: number;
    pressure: number;
    windStrength: number;
    windDirection: number;
    id: number;
    hasSensor: boolean;
    forestFireIndex: String;
    forestFireStatus: ForestFireState;
    isBeingExtinguished: boolean;
    isBeingBurned: boolean;
}
