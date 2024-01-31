package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.ADIS16448_IMU;

public class FalconSubsytem extends SubsystemBase {
    TalonFX falcon;

    ADIS16448_IMU gyro = new ADIS16448_IMU();

    public FalconSubsytem() {
        falcon = new TalonFX(3);
    }

    public void setFalcon(double speed) {
        falcon.set(speed);
    }

    public void resetGyro() {
        gyro.reset();
    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {

    }
}
