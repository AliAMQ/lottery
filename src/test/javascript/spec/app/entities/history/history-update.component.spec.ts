/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LotteryTestModule } from '../../../test.module';
import { HistoryUpdateComponent } from 'app/entities/history/history-update.component';
import { HistoryService } from 'app/entities/history/history.service';
import { History } from 'app/shared/model/history.model';

describe('Component Tests', () => {
    describe('History Management Update Component', () => {
        let comp: HistoryUpdateComponent;
        let fixture: ComponentFixture<HistoryUpdateComponent>;
        let service: HistoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [HistoryUpdateComponent]
            })
                .overrideTemplate(HistoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HistoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HistoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new History(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.history = entity;
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
                    const entity = new History();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.history = entity;
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
