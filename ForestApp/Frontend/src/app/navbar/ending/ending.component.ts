import { Summary } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { ForestPixelService } from 'src/app/forest-pixel.service';
import { ISummary } from 'src/app/models/summary';

@Component({
  selector: 'app-ending',
  templateUrl: './ending.component.html',
  styleUrls: ['./ending.component.scss']
})
export class EndingComponent implements OnInit {

  summary!: ISummary;

  constructor(public forestPixel: ForestPixelService) { }

  ngOnInit(): void {
    this.forestPixel.endSimulation().subscribe(date => {
      this.summary = date;
    })
    
  }

}
