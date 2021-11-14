import { NgModule } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTabsModule} from '@angular/material/tabs';
import {MatInputModule} from '@angular/material/input';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatSliderModule} from '@angular/material/slider';
import { MatIconModule} from '@angular/material/icon';
import { MatTooltipModule} from '@angular/material/tooltip';
import { ScrollingModule } from '@angular/cdk/scrolling';
import {MatDialogModule} from '@angular/material/dialog';

const MaterialComponents = [
  MatButtonModule,
  ScrollingModule,
  MatDialogModule,
  MatTabsModule,
  MatTooltipModule,
  MatInputModule,
  MatExpansionModule,
  MatToolbarModule,
  MatSliderModule,
  MatIconModule
];
@NgModule({
  exports: [MaterialComponents],
  imports: [
    MaterialComponents
  ]
})
export class MaterialModule { }
