import { ForestPixelService } from 'src/app/forest-pixel.service';
import { Component, OnInit } from '@angular/core';
import { EndingComponent } from './ending/ending.component';
import { MatDialog } from '@angular/material/dialog';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private forestPixelService: ForestPixelService, public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  startSimulation(){
    this.forestPixelService.startSimulation();
  }
  newSimulation(){
    this.forestPixelService.newSimulation();
  }
  endSimulation(){
    this.dialog.open(EndingComponent);
  }

}
