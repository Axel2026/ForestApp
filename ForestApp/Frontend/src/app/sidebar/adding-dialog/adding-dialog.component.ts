import { Component, OnInit } from '@angular/core';
import { ForestPixelService } from 'src/app/forest-pixel.service';

@Component({
  selector: 'app-adding-dialog',
  templateUrl: './adding-dialog.component.html',
  styleUrls: ['./adding-dialog.component.scss']
})
export class AddingDialogComponent implements OnInit {

  board: any;
  isClicked = false;
  constructor(public forestPixel: ForestPixelService) { }


  ngOnInit(): void {

    this.forestPixel.getBoard().subscribe(date => {
      this.board = date;
    });
  }
  tickCell(col: number, row: number) {
   this.board[col][row].hasSensor = !this.board[col][row].hasSensor;

   console.log(this.board[col][row]);
  }

  updateBoard(){
    this.forestPixel.updateBoard(this.board);

  }

}
