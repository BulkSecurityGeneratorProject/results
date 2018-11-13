import { ICompSession } from 'app/shared/model//comp-session.model';
import { IAthlete } from 'app/shared/model//athlete.model';
import { IApparatus } from 'app/shared/model//apparatus.model';

export interface IScore {
  id?: number;
  total?: number;
  difficulty?: number;
  neutralDeductions?: number;
  session?: ICompSession;
  athlete?: IAthlete;
  apparatus?: IApparatus;
}

export const defaultValue: Readonly<IScore> = {};
