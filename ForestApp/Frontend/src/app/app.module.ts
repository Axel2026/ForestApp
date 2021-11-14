import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './material/material.module';
import { NavbarComponent } from './navbar/navbar.component';
import { Tab1Component } from './sidebar/tab1/tab1.component';
import { Tab2Component } from './sidebar/tab2/tab2.component';
import { BoardComponent } from './models/board/board.component';
import { AnnouncementComponent } from './models/announcement/announcement.component';
import { HttpClientModule } from '@angular/common/http';
import { AddingDialogComponent } from './sidebar/adding-dialog/adding-dialog.component';
import { AddingFireComponent } from './sidebar/adding-fire/adding-fire.component';
import { EndingComponent } from './navbar/ending/ending.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    Tab1Component,
    Tab2Component,
    BoardComponent,
    AnnouncementComponent,
    AddingDialogComponent,
    AddingFireComponent,
    EndingComponent
  ],
  entryComponents: [AddingDialogComponent],

  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
