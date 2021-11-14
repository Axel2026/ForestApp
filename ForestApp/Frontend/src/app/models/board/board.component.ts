import { ForestFireState } from '../forest-fire-state';
declare var require: any;
import { ForestPixelService } from './../../forest-pixel.service';
import { Component, OnInit } from '@angular/core';
import { IForestPixel } from '../forest-pixel';
const rxjs = require('rxjs');
const { interval } = rxjs;

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  board: any;
  forestFireState = ForestFireState;


  constructor(public forestPixel: ForestPixelService) { }


  ngOnInit(): void {
    this.updateBoard();
    interval(1000).subscribe(() => {
      this.forestPixel.getBoard().subscribe(date => {
        this.board = date;
      });
    });
  }
  updateBoard(): void {
    this.forestPixel.getBoard().subscribe(date => {
      this.board = date;
    });
  }




}
