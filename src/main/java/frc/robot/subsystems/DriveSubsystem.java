// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import frc.robot.commands.StopCommand;

public class DriveSubsystem extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */

    ADIS16448_IMU gyro = new ADIS16448_IMU();

    Translation2d exampleModuleLocation = new Translation2d(0.3048, 0.3048);

    SwerveModule exampleModule = new SwerveModule(3, 4, false, true);

    public SwerveDriveKinematics kinematics = new SwerveDriveKinematics(exampleModuleLocation);

    SwerveDriveOdometry odometry = new SwerveDriveOdometry(
            kinematics, getRotation2d(), new SwerveModulePosition[] {
                    exampleModule.getPosition(),
            },
            new Pose2d(0, 0, new Rotation2d()));

    public DriveSubsystem() {
        gyro.calibrate();
        setDefaultCommand(new StopCommand(this));
    }

    @Override
    public void periodic() {
        odometry.update(getRotation2d(),
                new SwerveModulePosition[] {
                        exampleModule.getPosition()
                });
    }

    public void setModuleStatesFromSpeeds(double xVelocity, double yVelocity, double angularVelocity,
            boolean isFieldCentric) {
        ChassisSpeeds speeds;
        if (isFieldCentric) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(xVelocity, -yVelocity, angularVelocity, getRotation2d());
        } else {
            speeds = new ChassisSpeeds(xVelocity, -yVelocity, angularVelocity);
        }
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        // set speed/max velocity here.
        SwerveDriveKinematics.desaturateWheelSpeeds(states, 3);
        setmoduleStates(states);

    }

    public void resetGyro() {
        gyro.reset();
    }

    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(getRotation2d(),
                new SwerveModulePosition[] {
                        exampleModule.getPosition()
                }, pose);
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public Rotation2d getRotation2d() {
        return new Rotation2d(-gyro.getGyroAngleZ() / 57.295779513);
    }

    public double getGyroAngleY() {
        return gyro.getGyroAngleY();
    }

    public double getGyroRate() {
        return gyro.getGyroRateY();
    }

    public void setmoduleStates(SwerveModuleState[] moduleStates) {
        exampleModule.setDesiredState(moduleStates[0]);
    }

    public void stop() {
        exampleModule.stopModule();
    }

    public void setModules(double driveVolts, double turnVolts) {
        exampleModule.setModule(driveVolts, turnVolts);
    }
}