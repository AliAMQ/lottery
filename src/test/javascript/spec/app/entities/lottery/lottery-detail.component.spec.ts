/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotteryTestModule } from '../../../test.module';
import { LotteryDetailComponent } from 'app/entities/lottery/lottery-detail.component';
import { Lottery } from 'app/shared/model/lottery.model';

describe('Component Tests', () => {
    describe('Lottery Management Detail Component', () => {
        let comp: LotteryDetailComponent;
        let fixture: ComponentFixture<LotteryDetailComponent>;
        const route = ({ data: of({ lottery: new Lottery(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [LotteryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LotteryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LotteryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.lottery).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
