/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotteryTestModule } from '../../../test.module';
import { HistoryDetailComponent } from 'app/entities/history/history-detail.component';
import { History } from 'app/shared/model/history.model';

describe('Component Tests', () => {
    describe('History Management Detail Component', () => {
        let comp: HistoryDetailComponent;
        let fixture: ComponentFixture<HistoryDetailComponent>;
        const route = ({ data: of({ history: new History(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [HistoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HistoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HistoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.history).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
