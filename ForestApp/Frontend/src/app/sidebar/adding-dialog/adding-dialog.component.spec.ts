import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddingDialogComponent } from './adding-dialog.component';

describe('AddingDialogComponent', () => {
  let component: AddingDialogComponent;
  let fixture: ComponentFixture<AddingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddingDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
