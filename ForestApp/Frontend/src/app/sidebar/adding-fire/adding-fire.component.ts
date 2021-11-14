import { ForestFireState } from './../../models/forest-fire-state';
import { Component, OnInit } from '@angular/core';
import { ForestPixelService } from 'src/app/forest-pixel.service';
import { FormBuilder, FormGroup, FormControl} from '@angular/forms';
@Component({
  selector: 'app-adding-fire',
  templateUrl: './adding-fire.component.html',
  styleUrls: ['./adding-fire.component.scss']
})
export class AddingFireComponent implements OnInit {

  board : any;
  isClicked: boolean = false;
  fireFormGroup: FormGroup;
  radioValue : FormControl = new FormControl();
  forestFireState = ForestFireState;
  constructor(public forestPixel: ForestPixelService, private fb: FormBuilder) {
    this.fireFormGroup = this.fb.group({
      radioValue : this.radioValue
    })
  }

  ngOnInit(): void {

    this.forestPixel.getBoard().subscribe(date => {
      this.board = date;
    })


  }
  tickCell(col: number, row: number) {
    console.log(this.board[col][row].forestFireState);
    if(this.board[col][row].forestFireState == ForestFireState.NONE){
   this.board[col][row].forestFireState = this.radioValue.value;
    }
    else {
      this.board[col][row].forestFireState = ForestFireState.NONE;
    }
    console.log(this.board);

  }

  updateBoard(){
    this.forestPixel.updateBoard(this.board);
    console.log(this.board);

  }
}
