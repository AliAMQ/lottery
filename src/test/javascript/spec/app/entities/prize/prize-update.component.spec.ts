/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LotteryTestModule } from '../../../test.module';
import { PrizeUpdateComponent } from 'app/entities/prize/prize-update.component';
import { PrizeService } from 'app/entities/prize/prize.service';
import { Prize } from 'app/shared/model/prize.model';

describe('Component Tests', () => {
    describe('Prize Management Update Component', () => {
        let comp: PrizeUpdateComponent;
        let fixture: ComponentFixture<PrizeUpdateComponent>;
        let service: PrizeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [PrizeUpdateComponent]
            })
                .overrideTemplate(PrizeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PrizeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrizeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Prize(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.prize = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Prize();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.prize = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
