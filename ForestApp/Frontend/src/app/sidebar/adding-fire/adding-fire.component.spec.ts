import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddingFireComponent } from './adding-fire.component';

describe('AddingFireComponent', () => {
  let component: AddingFireComponent;
  let fixture: ComponentFixture<AddingFireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddingFireComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddingFireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
