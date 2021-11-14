import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IBasicParameters } from './models/dto/basic-parameters';
import { IForestPixel } from './models/forest-pixel';
import { ISummary } from './models/summary';

@Injectable({
  providedIn: 'root'
})
export class ForestPixelService {

  constructor(private httpClient: HttpClient) {
   }

   getBoard(){
    return this.httpClient.get('http://localhost:8081/board');
   }
   updateBoard(board: IForestPixel[][]){
     this.httpClient.post<any>('http://localhost:8081/board', board).subscribe();
   }
   setCityName(cityName: String){
     return this.httpClient.post<String>('http://localhost:8081/city', cityName);
   }
   startSimulation(){
    this.httpClient.post<Boolean>('http://localhost:8081/start', true ).subscribe();
   }
   newSimulation(){
    this.httpClient.post<Boolean>('http://localhost:8081/new', true ).subscribe();
   }
   endSimulation(){
    return this.httpClient.get<ISummary>('http://localhost:8081/end');
   }
   updateParameters(basicParameters: IBasicParameters){
     return this.httpClient.post<IBasicParameters>('http://localhost:8081/forms', basicParameters);
   }
}
