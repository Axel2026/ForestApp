import { TestBed } from '@angular/core/testing';

import { ForestPixelService } from './forest-pixel.service';

describe('ForestPixelService', () => {
  let service: ForestPixelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ForestPixelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
