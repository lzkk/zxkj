package com.zxkj.common.practice.mode.behavior.responsibility;

class Client3 {
    public static void main(String[] args) {
        Engineer xiaoming = new Engineer.Builder()
                .addEngineer(new JuniorEngineer())
                .addEngineer(new MidEngineer())
                .addEngineer(new SeniorEngineer())
                .addEngineer(new ProfessionalEngineer())
                .build();
        IProject easyProject = new EasyProject();
        IProject normalProject = new NormalProject();
        IProject hardProject = new HardProject();
        IProject tooHardProject = new TooHardProject();
        IProject beyondProject = new BeyondProject();

        if (!xiaoming.doWork(easyProject)) {
            System.out.println("tell Product Manager: easy project can not complete");
        }
        if (!xiaoming.doWork(normalProject)) {
            System.out.println("tell Product Manager: normal project can not complete");
        }
        if (!xiaoming.doWork(hardProject)) {
            System.out.println("tell Product Manager: hard project can not complete");
        }
        if (!xiaoming.doWork(tooHardProject)) {
            System.out.println("tell Product Manager: too hard project can not complete");
        }
        if (!xiaoming.doWork(beyondProject)) {
            System.out.println("tell Product Manager: beyond project can not complete: " + beyondProject.difficulty());
        }
    }

    interface IProject {
        static int EASY = 0;
        static int NORMAL = 1;
        static int HARD = 2;
        static int TOO_HARD = 3;
        static int BEYOND = 4;

        // 项目难度
        int difficulty();
    }

    static class EasyProject implements IProject {

        @Override
        public int difficulty() {
            return IProject.EASY;
        }
    }

    static class NormalProject implements IProject {

        @Override
        public int difficulty() {
            return IProject.NORMAL;
        }
    }

    static class HardProject implements IProject {

        @Override
        public int difficulty() {
            return IProject.HARD;
        }
    }

    static class TooHardProject implements IProject {

        @Override
        public int difficulty() {
            return IProject.TOO_HARD;
        }
    }

    static class BeyondProject implements IProject {

        @Override
        public int difficulty() {
            return IProject.BEYOND;
        }
    }

    static abstract class Engineer {
        private Engineer mSuccessor;

        public final boolean doWork(IProject project) {
            boolean bRet = false;
            do {
                if (this.filterProject(project)) {
                    bRet = this.writeCode(project);
                    break;
                }
                if (this.mSuccessor != null) {
                    bRet = this.mSuccessor.doWork(project);
                    break;
                }
            } while (false);
            return bRet;
        }

        protected abstract boolean filterProject(IProject project);

        protected abstract boolean writeCode(IProject project);

        public static class Builder {
            private Engineer mFirst;
            private Engineer mLast;

            public Builder addEngineer(Engineer engineer) {
                if (this.mFirst == null) {
                    this.mFirst = this.mLast = engineer;
                } else {
                    this.mLast.mSuccessor = engineer;
                    this.mLast = engineer;
                }
                return this;
            }

            public Engineer build() {
                return this.mFirst;
            }
        }
    }

    // 初级工程师
    static class JuniorEngineer extends Engineer {

        @Override
        protected boolean filterProject(IProject project) {
            return project.difficulty() <= IProject.EASY;
        }

        @Override
        protected boolean writeCode(IProject project) {
            System.out.println("Junior engineer completes project: " + project.difficulty());
            return true;
        }
    }

    // 中级工程师
    static class MidEngineer extends Engineer {

        @Override
        protected boolean filterProject(IProject project) {
            return project.difficulty() <= IProject.NORMAL;
        }

        @Override
        protected boolean writeCode(IProject project) {
            System.out.println("Middle level engineer completes project: " + project.difficulty());
            return true;
        }
    }

    // 高级工程师
    static class SeniorEngineer extends Engineer {

        @Override
        protected boolean filterProject(IProject project) {
            return project.difficulty() <= IProject.HARD;
        }

        @Override
        protected boolean writeCode(IProject project) {
            System.out.println("Senior engineer completes project: " + project.difficulty());
            return true;
        }
    }

    // 资深工程师
    static class ProfessionalEngineer extends Engineer {

        @Override
        protected boolean filterProject(IProject project) {
            return project.difficulty() <= IProject.TOO_HARD;
        }

        @Override
        protected boolean writeCode(IProject project) {
            System.out.println("Professional engineer completes project: " + project.difficulty());
            return true;
        }
    }
}