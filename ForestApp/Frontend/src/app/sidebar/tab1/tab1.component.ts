import { AddingFireComponent } from './../adding-fire/adding-fire.component';
import { ForestPixelService } from 'src/app/forest-pixel.service';
import { HttpClient } from '@angular/common/http';
import { AddingDialogComponent } from './../adding-dialog/adding-dialog.component';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { IBasicParameters } from 'src/app/models/dto/basic-parameters';
@Component({
  selector: 'app-tab1',
  templateUrl: './tab1.component.html',
  styleUrls: ['./tab1.component.scss']
})
export class Tab1Component implements OnInit {

  board: any;
  windForm: FormGroup;
  basicForm: FormGroup;
  gasForm: FormGroup;
  searchForm: FormGroup;
  constructor(private fb: FormBuilder, private bf: FormBuilder, gf: FormBuilder, private sf: FormBuilder, public dialog: MatDialog,
    private forestPixelService: ForestPixelService) {

      this.updateBoard();


    this.windForm = this.fb.group({
      strength: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
      direction: 0

    });

    this.basicForm = this.bf.group({
      temperature: "",
      humidity: "",
      pressure: ""
    });

    this.gasForm = this.bf.group({
       co: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
       no: ["", [Validators.required, Validators.pattern('[0-9]+n.?[0-9]*')]],
       no2: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
       o3: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
      // so2: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
      // pm2_5: ["", [Validators.required, Validators.pattern('[0-9]+.?[0-9]*')]],
    });
    this.searchForm = this.bf.group({
      city: ["", [Validators.required, Validators.pattern('[a-zA-Z]+')]]
    })

   }

  ngOnInit(): void {
  }
  get windFormValue(){
    return this.windForm.controls;
  }
  get gasFormValue(){
    return this.gasForm.controls;
  }
  formatLabel(value: number){
    return value;
  }
  get searchFormValue(){
    return this.searchForm.controls;
  }
  openDialog(){
    this.dialog.open(AddingDialogComponent);
  }
  openFireDialog(){
    this.dialog.open(AddingFireComponent);
  }
  citySumbit(){
    var cityValue = this.searchForm.controls['city'].value;
    this.forestPixelService.setCityName(cityValue).subscribe(date => {

    })
    this.updateBoard();

  }
  basicParameters(){
    var parameters: IBasicParameters = {
       temperature: this.basicForm.controls['temperature'].value,
       humidity: this.basicForm.controls['humidity'].value,
       pressure: this.basicForm.controls['pressure'].value,
       windDirection: undefined,
       windStrength: undefined
    };
   this.forestPixelService.updateParameters(parameters).subscribe(date => {
     console.log(date);
   });

  }

  windParameters(){
    var parameters: IBasicParameters = {
       temperature: undefined,
       humidity: undefined,
       pressure: undefined,
       windDirection: this.windForm.controls['direction'].value,
       windStrength: this.windForm.controls['strength'].value
    };
   this.forestPixelService.updateParameters(parameters).subscribe(date => {
     console.log(date);
   });

  }

  updateValues(){
    this.basicForm.setValue({
      temperature: this.board[0][0].temperature,
      humidity: this.board[0][0].humidity,
      pressure: this.board[0][0].pressure
    });

    this.windForm.setValue({
      strength: this.board[0][0].windStrength,
      direction: this.board[0][0].windDirection
    });
  }

  updateBoard(){
    this.forestPixelService.getBoard().subscribe(date => {
      this.board = date;
      this.updateValues();
    })
  }

}
